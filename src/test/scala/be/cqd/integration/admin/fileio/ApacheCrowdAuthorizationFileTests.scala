package be.cqd.integration.admin.fileio

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import be.cqd.integration.admin.domain._
import javax.management.remote.rmi._RMIConnection_Stub
import org.omg.stub.javax.management.remote.rmi._RMIConnection_Stub

class ApacheCrowdAuthorizationFileTests extends Spec with ShouldMatchers {

  describe("An ApacheCrowdAuthorizationFile specification") {
    val input = List("[/xyz/tags/foo]\n", "foouser = rw", "@barGroup = r", "[/abc/branches/branch1]\n", "finger = r", "@hand = rw")

    describe("Ensure that 2 pairs are returned") {
      val element = new ApacheCrowdAuthorizationFile
      val configuredItems = element.getConfiguredItems(input)
      it("should return two pairs of configured items") {
        configuredItems.length should be(2)
      }
    }

    describe("Ensure SVN element is a tags element with a proper configuration") {
      val element = new ApacheCrowdAuthorizationFile
      val configuredItems = element.getConfiguredItems(input)
      it("should have a tag first") {
        val (svnElement, configuration) = configuredItems.head
        (svnElement.isInstanceOf[SvnTag] && configuration.length == 2) should be(true)
      }
    }

    describe("Ensure SVN element is a branch with a proper configuration") {
      val element = new ApacheCrowdAuthorizationFile
      val configuredItems = element.getConfiguredItems(input)
      it("should have a branch second") {
        val (svnElement, configuration) = configuredItems.tail.head
        (svnElement.isInstanceOf[SvnBranch] && configuration.length == 2) should be(true)
      }
    }

    describe("Read the configuration from a file") {
      val element = new ApacheCrowdAuthorizationFile
      val configuredItems = element.getConfiguration("src/test/resources/TestAuthzSVNCrowdAccessFile").map(_._1).size
      it("should be basically OK") {
        (configuredItems == 8) should be(true)
      }
    }

    describe("Write back the configuration to a file") {
      val authznFile = new ApacheCrowdAuthorizationFile
      val inputFile = "src/test/resources/TestAuthzSVNCrowdAccessFile"
      val outputFile = inputFile + ".out"
      val configuredItems = authznFile.getConfiguration(inputFile)
      authznFile.saveConfiguration(outputFile, configuredItems)
      val savedItems = authznFile.getConfiguration(outputFile)

      val svnElements = savedItems.map(_._1)
      val svnConfigItems = savedItems.map(_._2).flatten

      val branchX2 = svnElements.filter(_.isInstanceOf[SvnBranch]).size == 2
      val branchesX1 = svnElements.filter(_.isInstanceOf[SvnBranches]).size == 1
      val trunkX1 = svnElements.filter(_.isInstanceOf[SvnTrunk]).size == 1
      val tagsX1 = svnElements.filter(_.isInstanceOf[SvnTags]).size == 1
      val configX15 = svnConfigItems.size == 15

      it("should be the same data back out") {
        (branchX2 && branchesX1 && trunkX1 && tagsX1 && configX15) should be(true)
      }

    }
  }
}