package be.cqd.integration.admin.fileio

import be.cqd.integration.admin.domain._
import io.Source
import be.cqd.integration.admin.crowd.CrowdGroup
import java.io.{FileWriter, File}
import java.util.Date

class ApacheCrowdAuthorizationFile {

  def getConfiguration(fileName: String): List[Pair[SvnConfigurationElement, List[AuthorityWithPermissionSetting]]] = {
    val input = Source.fromFile(fileName).getLines.toList
            .filter(!_.trim.startsWith("#")).map(_.replaceAll("#.*", "")).filter(_.trim.length > 0) // Strip comments
    getConfiguredItems(input)
  }

  private def getNextAuthorityWithPermissionSetting(setting: String): AuthorityWithPermissionSetting = {
    setting match {
      case AuthorityWithPermissionSetting(authority, permission) => new AuthorityWithPermissionSetting(authority, permission)
      case CrowdGroup(group) => new AuthorityWithPermissionSetting(new CrowdGroup(group), Permission.NONE)
      case _ => throw new IllegalArgumentException("Bad setting: " + setting) // Really?  Option might be nicer...
    }
  }

  // Gets the first [thing like this] and returns the rest of the of the list items
  private def getNextElement(xs: List[String]): Pair[SvnConfigurationElement, List[String]] = {
    val element = xs.takeWhile(_.trim.startsWith("[")).head
    val rest = xs.dropWhile(_.trim.startsWith("["))

    val svnElement = element match {
      case SvnBranch(projectName, branchName) => new SvnBranch(projectName, branchName)
      case SvnTag(projectName, tagName) => new SvnTag(projectName, tagName)
      case SvnBranches(projectName) => new SvnBranches(projectName)
      case SvnTags(projectName) => new SvnTags(projectName)
      case SvnTrunk(projectName) => new SvnTrunk(projectName)
      case SvnProject(projectName) => new SvnProject(projectName)
      case SvnGroups(groups) => new SvnGroups("")
      case _ => new SvnRoot("")
    }

    Pair(svnElement, rest)
  }

  // Gets the items under first [thing like this] and
  // returns the rest of the of the list items before the next [thing like that]
  private def getNextConfiguration(xs: List[String]): Pair[List[AuthorityWithPermissionSetting], List[String]] = {
    val configuration = xs.dropWhile(_.trim.startsWith("[")).takeWhile(!_.startsWith("["))
    val rest = xs.dropWhile(!_.startsWith("["))

    val authoritiesWithPermissionSettings = configuration.map(getNextAuthorityWithPermissionSetting(_))

    Pair(authoritiesWithPermissionSettings, rest)
  }

  // TODO: make this private once the file IO is sorted

  // Get the configuration for each SVN element in the list.  Relies on ordering in the list that matches
  // the layout of the Apache SVN configuration file (see the sample in the Resources folder)
  def getConfiguredItems(lines: List[String]): List[Pair[SvnConfigurationElement, List[AuthorityWithPermissionSetting]]] = {
    val (svnElement, remainder) = getNextElement(lines)
    val (configuration, rest) = getNextConfiguration(remainder)

    val items = List.empty[Pair[SvnConfigurationElement, List[AuthorityWithPermissionSetting]]]

    if (rest == List.empty[String]) {
      (svnElement, configuration) :: items
    }
    else {
      List(Pair(svnElement, configuration)) ::: getConfiguredItems(rest)
    }
  }

  // TODO: Access the user in some way
  def saveConfiguration(fileName: String, configuration: List[Pair[SvnConfigurationElement, List[AuthorityWithPermissionSetting]]]) {
    val writer = new FileWriter(new File(fileName))

    try {
      writer.write("# Written by <some user> on " + new Date + "\n")
      writer.write(configuration.map(p => p._1.toString + p._2.mkString("\n") + "\n").mkString("\n"))
    } finally {
      writer.close
    }
  }
}