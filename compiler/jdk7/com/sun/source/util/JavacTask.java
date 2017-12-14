/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk7.com.sun.source.util;

import java.io.IOException;

import jdk7.javax.tools.JavaCompiler.CompilationTask;

import jdk7.com.sun.source.tree.CompilationUnitTree;
import jdk7.com.sun.source.tree.Tree;
import jdk7.javax.lang.model.element.Element;
import jdk7.javax.lang.model.type.TypeMirror;
import jdk7.javax.lang.model.util.Elements;
import jdk7.javax.lang.model.util.Types;
import jdk7.javax.tools.JavaFileObject;


/**
 * Provides access to functionality specific to the JDK Java Compiler, javac.
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public abstract class JavacTask implements CompilationTask {

    /**
     * Parse the specified files returning a list of abstract syntax trees.
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends CompilationUnitTree> parse()
        throws IOException;

    /**
     * Complete all analysis.
     *
     * @return a list of elements that were analyzed
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends Element> analyze() throws IOException;

    /**
     * Generate code.
     *
     * @return a list of files that were generated
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends JavaFileObject> generate() throws IOException;

    /**
     * The specified listener will receive events describing the progress of
     * this compilation task.
     */
    public abstract void setTaskListener(TaskListener taskListener);

    /**
     * Get a type mirror of the tree node determined by the specified path.
     */
    public abstract TypeMirror getTypeMirror(Iterable<? extends Tree> path);
    /**
     * Get a utility object for dealing with program elements.
     */
    public abstract Elements getElements();

    /**
     * Get a utility object for dealing with type mirrors.
     */
    public abstract Types getTypes();
}
