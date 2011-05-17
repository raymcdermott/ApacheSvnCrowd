package be.cqd.integration.admin.crowd

case class CrowdApacheConfigurationPerInstance(svnExternalRepositoryName: String = "None",
                                               svnInternalRepositoryName: String = "None",
                                               svnCrowdAccessFile: String = "None") extends CrowdApacheConfiguration {

  val EXTERNAL_REPOSITORY_NAME = "!!svn.external.reponame!!"
  val INTERNAL_REPOSITORY_NAME = "!!svn.internal.reponame!!"
  val CROWD_ACCESS_FILE = "!!crowd.access.file!!"

  val EXTERNAL_REPOSITORY_NOUN = "Location"
  val INTERNAL_REPOSITORY_NOUN = "SVNPath"
  val CROWD_ACCESS_FILE_NOUN = "AuthzSVNCrowdAccessFile"

  // Use for writing configuration out
  val updateMap = Map(EXTERNAL_REPOSITORY_NAME -> svnExternalRepositoryName,
    INTERNAL_REPOSITORY_NAME -> svnInternalRepositoryName,
    CROWD_ACCESS_FILE -> svnCrowdAccessFile)

  // Use for obtaining the current configuration
  val configMap = Set(EXTERNAL_REPOSITORY_NOUN, INTERNAL_REPOSITORY_NOUN, CROWD_ACCESS_FILE_NOUN)

  def getConfiguration(authznFileName: String) : Map[String, String] = {
    expandConfigurationItems(authznFileName, configMap)
    Map("","")
  }

  def saveConfiguration(configurationAsList: List[String]) {
    expandConfigurationItems(configurationAsList, updateMap)
  }


}

