package be.cqd.integration.admin.crowd

trait CrowdApacheConfiguration {

  def expandConfigurationItems(authznFileName: String, tokensToFind: List[String]) : Map[String, String] = {
    // Find the tokens and zip them with the lsist
    Map("","")
  }

  def expandConfigurationItems(configurationAsList: List[String], configurationMap: Map[String, String]) {
    configurationAsList.map(expandConfiguration(_))

    def expandConfiguration(config: String): String = {
      val matchingKeysIterator = configurationMap.keys.filter(config.contains(_)).iterator
      var resultString = config
      while (matchingKeysIterator.hasNext) {
        val nextKey = matchingKeysIterator.next
        resultString = resultString.replaceAll(nextKey, configurationMap(nextKey))
      }
      resultString
    }
  }

}