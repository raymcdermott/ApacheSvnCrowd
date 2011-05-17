package be.cqd.integration.admin.domain

case class SvnElementWithPermissionSettings(svnConfigurationElement: SvnConfigurationElement,
                                            authorityPermissionSettings: List[AuthorityWithPermissionSetting]) {
  override def toString = svnConfigurationElement.toString + authorityPermissionSettings.mkString("\n")
}