package be.cqd.integration.admin.crowd

import java.net.URI

class CrowdApacheConfigurationPerApplication extends CrowdApacheConfiguration {

  val crowdAppName = System.getProperty("crowd.app.name")
  val crowdAppPassword = System.getProperty("crowd.app.password")
  val crowdUrl = new URI(System.getProperty("crowd.url"))

  val CROWD_APP_NAME = "CROWD_APP_NAME"
  val CROWD_APP_PASSWD = "CROWD_APP_PASSWD"
  val CROWD_URL = "CROWD_URL"

  val CROWD_APP_NAME_KEY = "!!crowd.app.name!!"
  val CROWD_APP_PASSWD_KEY = "!!crowd.app.password!!"
  val CROWD_URL_KEY = "!!crowd.url!!"

  // Use this to write out the configuration
  val updateMap = Map(CROWD_APP_NAME_KEY -> crowdAppName,
          CROWD_APP_PASSWD_KEY -> crowdAppPassword,
          CROWD_URL_KEY -> crowdUrl)

  // Use this to expose the configuration
  val configMap = Map(CROWD_APP_NAME -> crowdAppName,
          CROWD_APP_PASSWD -> crowdAppPassword,
          CROWD_URL -> crowdUrl)

}
