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

package jdk7.com.sun.source.tree;

import jdk7.javax.lang.model.element.Name;

/**
 * A tree node for an identifier expression.
 *
 * For example:
 * <pre>
 *   <em>name</em>
 * </pre>
 *
 * @jls section 6.5.6.1
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public interface IdentifierTree extends ExpressionTree {
    Name getName();
}
