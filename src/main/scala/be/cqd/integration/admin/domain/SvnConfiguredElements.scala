package be.cqd.integration.admin.domain

import be.cqd.integration.admin.crowd.CrowdGroup

class SvnConfiguredElements {

  var svnElements = List.empty[SvnElementWithPermissionSettings]

  def addElement(element: SvnElementWithPermissionSettings) = {
    svnElements = element :: svnElements
  }

  private def getGroups: String = {
    val permissionSettings = svnElements.flatMap(_.authorityPermissionSettings).distinct
    val distinctCrowdGroups = permissionSettings.filter(_.authority.isInstanceOf[CrowdGroup]).distinct
    val distinctGroupNames = distinctCrowdGroups.map(_.authority.asInstanceOf[CrowdGroup].name).distinct

    if (distinctGroupNames.length == 0) ""
    else distinctGroupNames.mkString("[groups]\n", " =\n", " =\n")
  }

  override def toString = getGroups + svnElements.reverse.mkString("\n")

}