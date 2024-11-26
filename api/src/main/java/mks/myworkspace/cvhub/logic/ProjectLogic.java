/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.cvhub.logic;

import java.util.List;
import mks.myworkspace.cvhub.model.Item;

/**
 * An example logic interface
 * 
 * @author Mike Jennings (mike_jennings@unc.edu)
 *
 */
public interface ProjectLogic {

    /**
     * Get a list of Items
     * @return List of Items
     */
    public List<Item> getItems();

    /**
     * Get a single Item by ID
     * @param id Item ID
     * @return Item or null if not found
     */
    public Item getItemById(int id);

    /**
     * Add a new Item to the list
     * @param item Item to add
     * @return boolean indicating success
     */
    public boolean addItem(Item item);

    /**
     * Remove an Item by ID
     * @param id Item ID
     * @return boolean indicating success
     */
    public boolean removeItem(int id);
}
