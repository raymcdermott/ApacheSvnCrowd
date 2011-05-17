package be.cqd.integration.admin.svn

import scala.collection.JavaConversions._

import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient
import java.io.File
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import java.util.UUID
import org.tmatesoft.svn.core.{SVNCommitInfo, SVNDirEntry, SVNNodeKind, SVNURL}
import org.tmatesoft.svn.core.io.{SVNRepository, SVNRepositoryFactory}

class SvnDiskApi(locationOnDisk: String) {

  def createRepository(name: String): SVNRepository = {
    val adminClient = new SVNAdminClient(null.asInstanceOf[ISVNAuthenticationManager], null)
    val url = adminClient.doCreateRepository(new File(locationOnDisk + name), UUID.randomUUID.toString, true, false)
    getRepository(url)
  }

  def deleteRepository(repository: SVNRepository): Boolean = {

    def removeFiles(dir: File): Boolean = {
      val subDirectories = dir.listFiles.filter(_.isDirectory)
      subDirectories.forall(removeFiles(_))
      dir.listFiles.forall(_.delete)
      dir.delete
    }

    val root = new File(repository.getRepositoryRoot(true).getPath)
    removeFiles(root)
  }

  // Dangerous method... be careful about inlcuding in client
  def deleteRepository(repository: SVNURL): Boolean = {

    def removeFiles(dir: File): Boolean = {
      val subDirectories = dir.listFiles.filter(_.isDirectory)
      subDirectories.forall(removeFiles(_))
      dir.listFiles.forall(_.delete)
      dir.delete
    }

    val root = new File(repository.getPath)
    removeFiles(root)
  }

  def getRepository(url: String): SVNRepository = {
    getRepository(SVNURL.parseURIDecoded(url))
  }

  def getRepository(url: SVNURL): SVNRepository = {
    require(url.getProtocol.equals("file"))
    FSRepositoryFactory.setup
    SVNRepositoryFactory.create(url)
  }

  def validateRepository(repository: SVNRepository): Option[SVNURL] = {
    val nodeKind = repository.checkPath("", -1)
    if (nodeKind == SVNNodeKind.NONE || nodeKind == SVNNodeKind.FILE) None
    Some(repository.getRepositoryRoot(true))
  }

  def checkIfProjectLayoutIsStandard(repository: SVNRepository, project: String): Boolean = {
    val dirs = getSVNDirectories(repository, project).map(_._1)
    val root = "^" + project
    dirs.size == dirs.filter(s => s.matches(root + "/trunk.*") || s.matches(root + "/tags.*") || s.matches(root + "/branches.*")).size
  }

  def addDirectories(commitMessage: String, repository: SVNRepository, dirs: List[String]): SVNCommitInfo = {
    val editor = repository.getCommitEditor(commitMessage, null)
    editor.openRoot(-1)

    def addDirectory(dir: String) {
      editor.addDir(dir, null, -1)
      editor.closeDir
    }

    dirs.foreach(addDirectory(_))

    editor.closeDir // for root (yes it'svnConfigurationElement a horrible API)
    editor.closeEdit
  }

  def createStandardLayout(repository: SVNRepository, path: String) {
    val message = "Automatic creation of standard layout"
    addDirectories(message, repository, List(path + "trunk", path + "tags", path + "branches"))
  }

  def getSVNDirectories(repository: SVNRepository, path: String = ""): List[Pair[String, SVNDirEntry]] = {
    val entries = repository.getDir(path, -1L, null, null.asInstanceOf[java.util.Collection[SVNDirEntry]])
    val entriesList = entries.asInstanceOf[java.util.Collection[SVNDirEntry]].iterator.toList
    val directories = entriesList.filter(_.getKind == SVNNodeKind.DIR)

    def getNestedDirectoryEntries(entry: SVNDirEntry): List[Pair[String, SVNDirEntry]] = {
      val nextPath = if (path.equals("")) entry.getName else path + "/" + entry.getName
      val directories = getSVNDirectories(repository, nextPath)
      if (directories == List.empty)
        List(new Pair(nextPath, entry))
      else
        (nextPath, entry) :: directories
    }

    directories.flatMap(getNestedDirectoryEntries(_))
  }

}