package be.cqd.integration.admin.domain

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import be.cqd.integration.admin.crowd.{CrowdGroup, CrowdUser}

class AuthorityWithPermissionTests extends Spec with ShouldMatchers {

  describe("An Authority specification") {
    describe("with a user and default permission") {
      val authSpec = new AuthorityWithPermissionSetting(new CrowdUser("zekrom"))
      it("should have a user an equal sign and the READ_ONLY permission") {
        authSpec.toString should be("zekrom = r")
      }
    }

    describe("with a user and a non-default permission") {
      val authSpec = new AuthorityWithPermissionSetting(new CrowdUser("zekrom"), Permission.READ_WRITE)
      it("should have a user an equal sign and the permission") {
        authSpec.toString should be("zekrom = rw")
      }
    }

    describe("with a group") {
      val authSpec = new AuthorityWithPermissionSetting(new CrowdGroup("teamRocket"), Permission.READ_WRITE)
      it("should be like a user preceded by an @ sign") {
        authSpec.toString should be("@teamRocket = rw")
      }
    }

    describe("unapply a user configuration") {
      it("should create a class from a user string") {
        "zekrom = rw" match {
          case AuthorityWithPermissionSetting(authority, permission) => {
            new AuthorityWithPermissionSetting(authority, permission).authority.isInstanceOf[CrowdUser] should be(true)
          }
          case _ => fail("Extractor bug")
        }
      }
      it("should create a class from a group string") {
        "@teamRocket = r" match {
          case AuthorityWithPermissionSetting(authority, permission) =>
            new AuthorityWithPermissionSetting(authority, permission).authority.isInstanceOf[CrowdGroup] should be(true)
          case _ => fail("Extractor bug")
        }
      }
    }
  }
}
