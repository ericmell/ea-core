/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.svn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * 
 * @author tom
 */
public class SvnApi {

	static {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}
	private SVNRepository session = null;
	private String account;
	private String passwd;
	private SVNClientManager myClientManager = null;

	private SvnApi(String root, String account, String passwd) {
		this.session = getSession(root, account, passwd);
		this.account = account;
		this.passwd = passwd;

	}

	public static SvnApi getInstance(String root, String account, String passwd) {
		return new SvnApi(root, account, passwd);
	}

	private SVNRepository getSession(String url, String account, String passwd) {
		try {
			SVNURL svnUrl = SVNURL.parseURIEncoded(url);
			SVNRepository repository = SVNRepositoryFactory.create(svnUrl);
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(null, account, passwd,
							false);
			repository.setAuthenticationManager(authManager);
			return repository;
		} catch (SVNException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getConnacialRemotePath(String remotePath) {
		if (remotePath != null && !remotePath.equals("")) {
			if (!remotePath.substring(remotePath.length() - 1).equals("/")) {
				return remotePath + "/";
			}
		}
		return remotePath;
	}

	public boolean checkTagExists(String tagDirectory, String tagName)
			throws SVNException {
		return checkTagExists(tagDirectory, tagName, -1);
	}

	public boolean checkTagExists(String tagDirectory, String tagName,
			long version) throws SVNException {
		boolean result;
		String filePath = PathUtils.combindPath(tagDirectory, tagName);
		SVNNodeKind nodeKind = session.checkPath(filePath, version);

		// System.out.println(repository.getLocation());
		if (nodeKind == SVNNodeKind.DIR) {
			result = true;
		} else if (nodeKind == SVNNodeKind.FILE) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public void createDirectories(String dirPath) throws SVNException {
		if (dirPath == null) {
			throw new IllegalArgumentException("路径不允许为空");
		}

		String[] paths = dirPath.split("/");
		String dirToCreate = "";
		ISVNEditor editor = session.getCommitEditor("", null, true, null);
		editor.openRoot(-1);

		for (int i = 0; i < paths.length; i++) {
			if (i == 0) {
				dirToCreate = paths[i];
			} else {
				dirToCreate += "/" + paths[i];
			}

			try {
				editor.addDir(dirToCreate, null, -1);
			} catch (SVNException e) {
				if (e.getErrorMessage().getErrorCode()
						.equals(SVNErrorCode.RA_DAV_ALREADY_EXISTS)) {
					continue;
				} else {

					// e.printStackTrace();
					continue;
				}
			}

		}
		editor.closeDir();

		SVNCommitInfo commitInfo = editor.closeEdit();
	}

	public long addFile(String remoteDirectory, String fileName, File file,
			String description) throws FileNotFoundException, SVNException {

		// 增加目录和增加文件
		ISVNEditor editor = session.getCommitEditor(description, null);

		editor.openRoot(-1);
		String fileDir = getConnacialRemotePath(remoteDirectory);
		String fileFullSVNPath = fileDir + fileName;
		editor.openDir(fileDir, -1);
		editor.addFile(fileFullSVNPath, null, -1);

		editor.applyTextDelta(fileFullSVNPath, null);
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(fileFullSVNPath,
				new FileInputStream(file), editor, true);

		editor.closeFile(fileFullSVNPath, checksum);
		editor.closeDir();
		editor.closeDir();

		SVNCommitInfo commitInfo = editor.closeEdit();
		return commitInfo.getNewRevision();

	}

	/**
	 * 更新文件。
	 * 
	 * @param remoteDirectory
	 *            新文件在Subversion服务器上所在的目录
	 * @param fileName
	 *            新文件在Subversion服务器上的文件名
	 * @param file
	 *            本地待提交的文件
	 * @param description
	 *            提交说明
	 * 
	 * @return 提交完成后的新版本号
	 * @throws SVNException
	 * @throws FileNotFoundException
	 */
	public long updateFile(String remoteDirectory, String remoteFileName,
			File file, String description) throws SVNException,
			FileNotFoundException {
		// 获取最终版本号

		// 增加目录和增加文件
		ISVNEditor editor = session.getCommitEditor(description, null);
		editor.openRoot(-1);
		String fileDir = getConnacialRemotePath(remoteDirectory);
		String fileFullSVNPath = fileDir + remoteFileName;
		editor.openDir(fileDir, -1);
		editor.openFile(fileFullSVNPath, -1);
		editor.applyTextDelta(fileFullSVNPath, null);
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(fileFullSVNPath,
				new FileInputStream(file), editor, true);
		editor.closeFile(fileFullSVNPath, checksum);
		editor.closeDir();
		editor.closeDir();

		SVNCommitInfo commitInfo = editor.closeEdit();

		return commitInfo.getNewRevision();
	}

	protected SVNClientManager getClientManager() {
		if (myClientManager == null) {
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(null, this.account,
							this.passwd, false);
			myClientManager = SVNClientManager
					.newInstance(options, authManager);
		}
		return myClientManager;
	}

	public void delete(String url) throws SVNException {

		SVNURL srcURL = SVNURL.parseURIEncoded(url);
		SVNCommitClient commit = getClientManager().getCommitClient();
		commit.doDelete(new SVNURL[] { srcURL }, "删除旧版本");
	}

	public void copy(String srcURLStr, long version, String dstURLStr,
			String commitMessage) throws SVNException {
		SVNRevision srcRevision = SVNRevision.create(version);
		SVNURL srcURL = SVNURL.parseURIEncoded(srcURLStr);
		SVNURL dstURL = SVNURL.parseURIEncoded(dstURLStr);

		SVNCopySource svnCopySource = new SVNCopySource(SVNRevision.UNDEFINED,
				srcRevision, srcURL);
		try {
			SVNCopyClient copy = getClientManager().getCopyClient();
			SVNCommitInfo result = copy.doCopy(
					new SVNCopySource[] { svnCopySource }, dstURL, false, true,
					true, commitMessage, new SVNProperties());
			// this.checkoutLog.append(srcURLStr).append("\r\n");

		} catch (SVNException e) {
			if (e.getErrorMessage().getErrorCode()
					.equals(SVNErrorCode.RA_DAV_PATH_NOT_FOUND)) {
				System.out.println(e.getErrorMessage().getErrorCode());
			} else if (e.getErrorMessage().getErrorCode()
					.equals(SVNErrorCode.FS_ALREADY_EXISTS)) {
				delete(dstURLStr);
				copy(srcURLStr, version, dstURLStr, commitMessage);
			} else {
				throw new SVNException(e.getErrorMessage());
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			SVNException {

		String svnUrl = "http://localhost/svn/train/project";
		String account = "tom";
		String passwd = "123";
		SvnApi svn = SvnApi.getInstance(svnUrl, account, passwd);
		svn.createDirectories("test2");
	}

	public String combindPath(String a, String b) {
		if (a == null || a.length() == 0) {
			a = "";
		} else {
			if (a.startsWith("/")) {
				a = a.substring(1, a.length());
			}
			if (a.endsWith("/")) {
				a = a.substring(0, a.length() - 1);
			}
		}

		if (b == null || b.length() == 0) {
			b = "";
		} else {
			if (b.startsWith("/")) {
				b = b.substring(1, b.length());
			}
			if (b.endsWith("/")) {
				b = b.substring(0, b.length() - 1);
			}
		}

		if (a.equals("")) {
			return b;
		}

		if (b.equals("")) {
			return a;
		}

		return a + "/" + b;
	}

	public void addAllFile(String tmpPath, File file) throws SVNException,
			FileNotFoundException {
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {

				String relPath = files[i].getParent();

				relPath = relPath.replaceAll("\\\\", "/");
				relPath = relPath.replaceAll(tmpPath, "");

				if (relPath.startsWith("/")) {
					relPath = relPath.substring(1, relPath.length());
				}

				if (files[i].isFile()) {

					if (!checkTagExists(relPath, "")) {
						createDirectories(relPath);
					}

					if (checkTagExists(relPath, files[i].getName(), -1)) {
						updateFile(relPath, files[i].getName(), files[i], null);
					} else {
						addFile(relPath, files[i].getName(), files[i], null);
					}

				} else {
					addAllFile(tmpPath, files[i]);
				}
			}
		}
	}
}
