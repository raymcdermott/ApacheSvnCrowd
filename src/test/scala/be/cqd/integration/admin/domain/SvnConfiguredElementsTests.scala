package be.cqd.integration.admin.domain

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import be.cqd.integration.admin.crowd.{CrowdGroup, CrowdUser}

class SvnConfiguredElementsTests extends Spec with ShouldMatchers {

  describe("An SvnConfiguredElements specification") {
    describe("as a Crowd config file with 0 groups") {
      val authSpecs = new AuthorityWithPermissionSetting(new CrowdUser("ray")) :: Nil
      val project = new SvnProject("project99")
      val elements = new SvnConfiguredElements
      elements.addElement(new SvnElementWithPermissionSettings(project, authSpecs))

      it("should have a project spec, with no groups stanza") {
        elements.toString should be("[/project99]\nray = r")
      }
    }

    describe("as a Crowd config file with 1 group") {
      val authSpecs = new AuthorityWithPermissionSetting(new CrowdGroup("teamRocket")) :: Nil
      val project = new SvnProject("project99")
      val elements = new SvnConfiguredElements
      elements.addElement(new SvnElementWithPermissionSettings(project, authSpecs))

      it("should have the groups stanza preceding the spec with 1 group") {
        elements.toString should be("[groups]\nteamRocket =\n[/project99]\n@teamRocket = r")
      }
    }

    describe("as a Crowd config file with 2 elements and > 1 group") {
      val authSpecs = new AuthorityWithPermissionSetting(new CrowdGroup("teamRocket")) ::
              new AuthorityWithPermissionSetting(new CrowdGroup("teamFoo"), Permission.READ_WRITE) ::
              Nil

      val projectName = "project99"

      val project = new SvnProject(projectName)

      val elements = new SvnConfiguredElements

      elements.addElement(new SvnElementWithPermissionSettings(project, authSpecs))

      val trunk = new SvnTrunk(projectName)

      elements.addElement(new SvnElementWithPermissionSettings(trunk, authSpecs))

      it("should have a distinct list of the groups preceding the spec") {
        elements.toString should be("[groups]\nteamRocket =\nteamFoo =\n" +
                "[/project99]\n@teamRocket = r\n@teamFoo = rw\n" +
                "[/project99/trunk]\n@teamRocket = r\n@teamFoo = rw")
      }
    }
  }
}