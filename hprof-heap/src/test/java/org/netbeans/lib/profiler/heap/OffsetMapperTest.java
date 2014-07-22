/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Alexey Ragozin
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package org.netbeans.lib.profiler.heap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class OffsetMapperTest {

    Heap heap;

    @Before
    public void initHeap() throws FileNotFoundException, IOException {
        heap = HeapFactory.createHeap(HeapDumpProcuder.getHeapDump());
    }

    @Test
    public void verify_heap_offsets_hashed() {
        HashSet<Long> instances = new HashSet<Long>();
        for(Instance i: heap.getAllInstances()) {
            instances.add(i.getInstanceId());
        }

        HeapOffsetMap map = new HeapOffsetMap((HprofHeap)heap);

        for(long id: instances) {
            long offs = map.offset(id);
            Assert.assertTrue(offs > 0);
            Assert.assertEquals(id, readID(offs));
        }

        System.out.println("Heap instances: " + instances.size());
    }

    @Test
    public void verify_heap_offsets_ordered() {
        Set<Long> instances = new TreeSet<Long>();
        for(Instance i: heap.getAllInstances()) {
            instances.add(i.getInstanceId());
        }

        HeapOffsetMap map = new HeapOffsetMap((HprofHeap)heap);

        for(long id: instances) {
            long offs = map.offset(id);
            Assert.assertTrue(offs > 0);
            Assert.assertEquals(id, readID(offs));
        }

        System.out.println("Heap instances: " + instances.size());
    }

//    @Test
//    public void verify_heap_offsets_for_classes() {
//        Set<Long> instances = new TreeSet<Long>();
//        for(JavaClass i: heap.getAllClasses()) {
//            instances.add(i.getJavaClassId());
//        }
//
//        HeapOffsetMap map = new HeapOffsetMap((HprofHeap)heap);
//
//        for(long id: instances) {
//            long offs = map.offset(id);
//            Assert.assertTrue(offs > 0);
//            Assert.assertEquals(id, readID(offs));
//        }
//
//        System.out.println("Heap classes: " + instances.size());
//    }

    private long readID(long offset) {
        long[] pointer = new long[]{offset};
        HprofHeap heap = (HprofHeap) this.heap;
        TagBounds bounds = heap.getAllInstanceDumpBounds();

        while(pointer[0] < bounds.endOffset) {
            long ptr = pointer[0];
            int tag = heap.readDumpTag(pointer);

            if (   tag == HprofHeap.INSTANCE_DUMP
                || tag == HprofHeap.OBJECT_ARRAY_DUMP
                || tag == HprofHeap.PRIMITIVE_ARRAY_DUMP) {
                return heap.dumpBuffer.getID(ptr + 1);
            }
        }
        return -1;
    }
}
