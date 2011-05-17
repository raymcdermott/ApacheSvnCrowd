package be.cqd.integration.admin.domain

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

class SvnConfigurationTests extends Spec with ShouldMatchers {

  describe("An configuration specification") {

    describe("SvnRoot with a root") {
      val element = new SvnRoot("")
      it("should have [/] on one line followed by newline") {
        element.toString() should be("[/]\n")
      }
    }

    val svnRootAsString = "[/]\n"

    describe("a good extractor for a SvnRoot") {
      it("should match " + svnRootAsString) {
        val root = svnRootAsString match {
          case SvnRoot(root) => new SvnRoot(root)
          case _ => "Match Test Fails"
        }
        root.toString should be(svnRootAsString)
      }
    }

    describe("a bad extractor for a SvnRoot") {
      it("should not match [/anythingHere]") {
        "[/anythingHere]" match {
          case SvnRoot(root) => false should be(true)
          case _ => true should be(true)
        }
      }
    }

    val canonicalSvnProject = "[/someProject]\n"

    describe("SvnProject with a project name") {
      val element = new SvnProject("someProject")
      it("should have " + canonicalSvnProject + " on one line followed by newline") {
        element.toString() should be(canonicalSvnProject)
      }
    }

    describe("a good extractor for a SvnProject") {
      it("should match" + canonicalSvnProject) {
        val svnProject = canonicalSvnProject match {
          case SvnProject(projectName) => new SvnProject(projectName)
          case _ => "Match Test Fails"
        }
        svnProject.toString should be(canonicalSvnProject)
      }
      it("should match [/someProject/branch]") {
        "[/someProject/branch]" match {
          case SvnProject(projectName) => true should be(true)
          case _ => false should be(true)
        }
      }
    }

    describe("a bad extractor for a SvnProject") {
      it("should not match [/]") {
        "[/]" match {
          case SvnProject(projectName) => false should be(true)
          case _ => true should be(true)
        }
      }
    }

    val canonicalSvnTrunk = "[/someProject/trunk]\n"

    describe("SvnTrunk with a project name and a trunk") {
      val element = new SvnTrunk("someProject")
      it("should have " + canonicalSvnTrunk + " on one line followed by newline") {
        element.toString() should be(canonicalSvnTrunk)
      }
    }

    describe("SvnTrunk with a an existing project") {
      val element = new SvnTrunk(new SvnProject("someProject"))
      it("should have " + canonicalSvnTrunk + " on one line followed by newline") {
        element.toString() should be(canonicalSvnTrunk)
      }
    }

    describe("a good extractor for a SvnTrunk") {
      it("should match " + canonicalSvnTrunk) {
        val svnTrunk = canonicalSvnTrunk match {
          case SvnTrunk(projectName) => new SvnTrunk(projectName)
          case _ => "Match Test Fails"
        }
        svnTrunk.toString should be(canonicalSvnTrunk)
      }
    }

    describe("a bad extractor for a SvnTrunk") {
      it("should not match [/someProject/branch]") {
        "[/someProject/branch]" match {
          case SvnTrunk(projectName) => false should be(true)
          case _ => true should be(true)
        }
      }
      it("should not match [/]") {
        "[/]" match {
          case SvnTrunk(projectName) => false should be(true)
          case _ => true should be(true)
        }
      }
    }

    val canonicalBranchName = "[/someProject/branches/branchName]\n"

    describe("SvnBranch with a project name and a branch") {
      val element = new SvnBranch("someProject", "branchName")
      it("should have " + canonicalBranchName + " on one line followed by newline") {
        element.toString() should be(canonicalBranchName)
      }
    }

    describe("Matchng a good extractor for a SvnBranch") {
      it("should match " + canonicalBranchName) {
        val branch = canonicalBranchName match {
          case SvnBranch(project, branch) => new SvnBranch(project, branch)
          case _ => "Match Test Fails"
        }
        branch.toString should be(canonicalBranchName)
      }
    }

    describe("bad inputs for the SvnBranch extractor") {
      it("should not match [/someProject/trunk]") {
        "[/someProject/branch]" match {
          case SvnBranch(projectName, branch) => false should be(true)
          case _ => true should be(true)
        }
      }
      it("should not match [/]") {
        "[/]" match {
          case SvnBranch(projectName, branch) => false should be(true)
          case _ => true should be(true)
        }
      }
    }

    val canonicalTagName = "[/someProject/tags/tagName]\n"

    describe("SvnTag with a project name and a tag") {
      val element = new SvnTag("someProject", "tagName")
      it("should have "  + canonicalTagName + " on one line followed by newline") {
        element.toString() should be(canonicalTagName)
      }
    }

    describe("a good extractor for a SvnTag") {
      it("should match " + canonicalTagName) {
        val tag = canonicalTagName match {
          case SvnTag(project, tag) => new SvnTag(project, tag)
          case _ => "Match Test Fails"
        }
        tag.toString should be(canonicalTagName)
      }
    }

    describe("bad inputs for the SvnTag extractor") {
      it("should not match [/someProject/trunk]") {
        "[/someProject/trunk]" match {
          case SvnTag(projectName, tag) => false should be(true)
          case _ => true should be(true)
        }
      }
      it("should not match [/]") {
        "[/]" match {
          case SvnTag(projectName, tag) => false should be(true)
          case _ => true should be(true)
        }
      }
    }

  }
}
