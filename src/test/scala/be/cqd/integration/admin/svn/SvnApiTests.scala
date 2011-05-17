package be.cqd.integration.admin.svn

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.tmatesoft.svn.core.io.SVNCapability
import org.tmatesoft.svn.core.SVNDirEntry

class SvnApiTests extends Spec with ShouldMatchers {

  //  // TODO: Plug this as a property
  val locationOnDisk = "/Users/ray/Development/svn/repos/"

  describe("The SVN api should have the ability to") {

    describe("create and then delete a repository") {
      val api = new SvnDiskApi(locationOnDisk)
      val repo = api.createRepository("repoForDeletion")
      it("should be cleanly removed") {
        api.deleteRepository(repo) should be(true)
      }
    }

    describe("create directories in a repository") {
      val api = new SvnDiskApi(locationOnDisk)
      val repo = api.createRepository("repoForTestThenDeletion")
      it("should have a directory added") {
        api.addDirectories("no comment", repo, List("/directoryName")).toString.contains(" at ") should be(true)
      }
      it("should be able to find the added directory") {
        val directories = api.getSVNDirectories(repo, "")
        directories.filter(_._1.matches("directoryName")).size should be(1)
      }
      it("should have files added and then, the repo, should be removed") {
        api.deleteRepository(repo) should be(true)
      }
    }

    describe("create standard directories in a repository under a project") {
      val api = new SvnDiskApi(locationOnDisk)
      val repo = api.createRepository("repoWithStandardLayoutForTestThenDeletion")

      val projectName = "/projectName"

      it("should have a directory added") {
        api.addDirectories("no comment", repo, List(projectName)).toString.contains(" at ") should be(true)
      }

      api.createStandardLayout(repo, projectName)

      it("should be able to confirm the standard layout") {
        api.checkIfProjectLayoutIsStandard(repo, projectName) should be(true)
      }

      val list = api.getSVNDirectories(repo)
      it("is a non zero length list of pairs showing directories and SVN Entries") {
        list.size > 0 && list.head.isInstanceOf[Pair[String, SVNDirEntry]] should be(true)
      }

      it("should have files added and then, the repo, should be removed") {
        api.deleteRepository(repo) should be(true)
      }
    }

  }

}