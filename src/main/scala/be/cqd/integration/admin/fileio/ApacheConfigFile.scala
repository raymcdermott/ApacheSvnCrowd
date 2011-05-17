package be.cqd.integration.admin.fileio

import io.Source
import be.cqd.integration.admin.crowd.{CrowdApacheConfigurationPerInstance, CrowdApacheConfigurationPerApplication}

class ApacheConfigFile {

  def getConfiguration(fileName: String): Map[String, String] = {
    Source.fromFile(fileName).getLines.toList

    // The app should be configured in the environment
    val perApp = new CrowdApacheConfigurationPerApplication().configMap

    val perInstance = new CrowdApacheConfigurationPerInstance().configMap

    Map("","")
  }

  def saveConfiguration(svnExternalRepositoryName: String,
                        svnInternalRepositoryName: String,
                        svnCrowdAccessFile: String) {

  }


  // TODO: how to hook this into the apache config infra?

}