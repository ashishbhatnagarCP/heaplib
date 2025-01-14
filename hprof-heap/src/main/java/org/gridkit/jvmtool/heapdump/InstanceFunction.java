/**
 * Copyright 2021 Alexey Ragozin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gridkit.jvmtool.heapdump;

import org.netbeans.lib.profiler.heap.FieldValue;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.PrimitiveArrayInstance;

/**
 * Function can be used as a terminal step in
 * @author Alexey Ragozin (alexey.ragozin@gmail.com)
 *
 */
interface InstanceFunction {

    public Object apply(Instance i);

    public Object applyToField(Instance i, FieldValue fv);

    public Object applyToArray(PrimitiveArrayInstance array, int index);
}
