package be.cqd.integration.admin.domain

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import be.cqd.integration.admin.crowd.CrowdUser

class SvnElementWithPermissionSettingsTests extends Spec with ShouldMatchers {

  describe("An SvnElementWithPermissionSettings specification") {
    describe("with a project") {
      val project = new SvnProject("project99")
      val authSpecs = new AuthorityWithPermissionSetting(new CrowdUser("ray")) ::
                      new AuthorityWithPermissionSetting(new CrowdUser("erica"), Permission.READ_WRITE) ::
                      Nil
      val element = new SvnElementWithPermissionSettings(project, authSpecs)
      it("should have a project on one line followed by the two user and permission settings") {
        element.toString should be("[/project99]\nray = r\nerica = rw")
      }
    }
  }
}