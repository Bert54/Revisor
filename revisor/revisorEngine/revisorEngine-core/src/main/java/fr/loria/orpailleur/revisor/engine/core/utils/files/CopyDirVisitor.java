package fr.loria.orpailleur.revisor.engine.core.utils.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author William Philbert
 */
public class CopyDirVisitor extends SimpleFileVisitor<Path> {
	
	private Path source;
	private Path target;
	private boolean deleteOnExit;
	private CopyOption[] options;
	
	public CopyDirVisitor(Path source, Path target, boolean deleteOnExit, CopyOption... options) {
		this.source = source;
		this.target = target;
		this.deleteOnExit = deleteOnExit;
		this.options = options;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		Path targetPath = resolve(this.target, this.source.relativize(dir));
		
		if(!Files.exists(targetPath)) {
			Files.createDirectory(targetPath);
		}
		
		if(this.deleteOnExit) {
			targetPath.toFile().deleteOnExit();
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Path targetPath = Files.copy(file, resolve(this.target, this.source.relativize(file)), this.options);
		
		if(this.deleteOnExit) {
			targetPath.toFile().deleteOnExit();
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	public static Path resolve(Path target, Path source) {
		if(source.getFileSystem().equals(target.getFileSystem())) {
			return target.resolve(source);
		}
		else if(source.toString().isEmpty()) {
			return target;
		}
		else {
			return (new File(target.toFile(), source.toString())).toPath();
		}
	}
	
}
