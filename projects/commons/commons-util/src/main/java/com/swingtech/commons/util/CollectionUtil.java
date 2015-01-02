/*
 * SwingTech Software - http://cooksarm.sourceforge.net/
 * 
 * Copyright (C) 2011 Joe Rice All rights reserved.
 * 
 * SwingTech Cooks Arm is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SwingTech Cooks Arm is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SwingTech Cooks Arm; If not, see <http://www.gnu.org/licenses/>.
 */
package com.swingtech.commons.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Javadoc Needed - create class documentation for: CollectionUtil.
 * 
 */
@SuppressWarnings("unchecked")
public class CollectionUtil {
    /**
     * Javadoc Needed - create constructor documentation.
     * 
     */
    public CollectionUtil() {
        super();
    }

    /**
     * A utility method that Searches for the first occurence of the given
     * argument, testing for equality using the equals method. This method is
     * similar to the indexOf() method in the java.util.List class. The
     * difference is that it allows you to specify which bean property to search
     * on.
     * 
     * For Example: <blockquote>
     * 
     * <pre>
     * Person  personArrayList[] = . . . &lt;font color=&quot;#339933&quot;&gt;//(get an array of populated Person objects from somewhere)&lt;/font&gt;
     * int     index = Utility.indexOfArray(personArrayList, &quot;age&quot;, &quot;1&quot;);
     * </pre>
     * 
     * </blockquote> returns the index of the first occurrence of the Person
     * object where <code>person.getAge().equals("1")</code>;
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanPropertyName
     *            - The property name to search for.
     * @param pValue
     *            - The value to search for.
     * @return int - returns the index of the first occurrence of the given
     *         argument.
     */
    public static int indexOfArray(final Object[] pArray, final String pBeanPropertyName, final Object pValue) {
        Object vArrayObj = null;
        Object vArrayValueObj = null;

        if (pArray == null || pArray.length <= 0 || pBeanPropertyName == null || "".equals(pBeanPropertyName.trim())
                || pValue == null)
        {
            return -1;
        }

        for (int i = 0; i < pArray.length; i++) {
            vArrayObj = pArray[i];
            vArrayValueObj = ClassUtil.getBeanPropertyString(vArrayObj, pBeanPropertyName);

            if (vArrayValueObj != null && vArrayValueObj.equals(pValue)) {
                return i;
            }
        }

        return -1;
    }

    public static List getListFromArray(final Object[] array) {
        List list = null;

        if (Utility.isNullOrEmpty(array)) {
            return null;
        }

        list = new ArrayList();

        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        return list;
    }

    /**
     * A utility method that Searches for the first occurence of the given
     * argument, testing for equality using the equals method. This method is
     * similar to the indexOf() method in the java.util.List class. The
     * difference is that it allows you to specify which bean property to search
     * on.
     * 
     * For Example: <blockquote>
     * 
     * <pre>
     * Person  personArrayList[] = . . . &lt;font color=&quot;#339933&quot;&gt;//(get an array of populated Person objects from somewhere)&lt;/font&gt;
     * int     index = Utility.indexOfArray(personArrayList, &quot;age&quot;, &quot;1&quot;, 3);
     * </pre>
     * 
     * </blockquote> returns the index of the first occurrence (after index #3)
     * of the Person object where <code>person.getAge().equals("1")</code>;
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanPropertyName
     *            - The property name to search for.
     * @param pValue
     *            - The value to search for.
     * @param pStartIndex
     *            - the non-negative index to start searching from.
     * @return int - returns the index of the first occurrence of the given
     *         argument.
     */
    public static int indexOfArray(final Object[] pArray, final String pBeanPropertyName, final Object pValue, final int pStartIndex) {
        Object vArrayObj = null;
        Object vArrayValueObj = null;

        if (pArray == null || pArray.length <= 0 || pBeanPropertyName == null || "".equals(pBeanPropertyName.trim())
                || pValue == null)
        {
            return -1;
        }

        if (pStartIndex >= pArray.length) {
            return -1;
        }

        if (pStartIndex < 0) {
            throw new IndexOutOfBoundsException("Index is negative number.  Index must be positive.");
        }

        for (int i = pStartIndex; i < pArray.length; i++) {
            vArrayObj = pArray[i];
            vArrayValueObj = ClassUtil.getBeanPropertyString(vArrayObj, pBeanPropertyName);

            if (vArrayValueObj != null && vArrayValueObj.equals(pValue)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * A utility method that searches through an array of objects and returns
     * all objects that have a property whose value matches the value passed in.
     * 
     * For example, <blockquote>
     * 
     * <pre>
     * Person personArrayList[] = . . . &lt;font color=&quot;#339933&quot;&gt;//(get an array of populated Person objects from somewhere)&lt;/font&gt;
     * Person returnPersonList[] = Utility.getObjectsFromArray(personList, &quot;age&quot;, &quot;1&quot;);
     * </pre>
     * 
     * </blockquote> returns all Person objects in the personList (array) where:
     * <code>person.getAge().equals("1")</code>
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanPropertyName
     *            - The property name to search for.
     * @param pValue
     *            - The value to search for.
     * @return Object[] - returns a sub-set of the original array.
     */
    public static Object[] getObjectsFromArray(final Object[] pArray, final String pBeanPropertyName, final Object pValue) {
        return getObjectsFromArray(pArray, pBeanPropertyName, pValue, -1);
    }

    /**
     * A utility method that searches through an array of objects and sets a
     * value for all objects that have a property whose value matches the filter
     * value passed in. Also returns an array of those matched values.
     * 
     * NOTE: even though this method returns a filtered array, the objects will
     * be changed in the original array passed in as well.
     * 
     * For example, <blockquote>
     * 
     * <pre>
     * Person personArrayList[] = . . . &lt;font color=&quot;#339933&quot;&gt;//(get an array of populated Person objects from somewhere)&lt;/font&gt;
     * Person returnPersonList[] = Utility.setObjectsInArray(personList,    &lt;font color=&quot;#339933&quot;&gt;// &lt;- The array &lt;/font&gt;
     *                                                       &quot;isSelected&quot;,  &lt;font color=&quot;#339933&quot;&gt;// &lt;- The bean property name on which to set a value&lt;/font&gt;
     *                                                       BOOLEAN.TRUE); &lt;font color=&quot;#339933&quot;&gt;// &lt;- The value to set &lt;/font&gt;
     * </pre>
     * 
     * </blockquote> sets all Person objects in the personList (array) with the
     * following value: <code>person.setIsSelected(BOOLEAN.TRUE)</code>
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanSetPropertyName
     * @param pSetValue
     * @return Object[] - returns the original array with the values changed.
     */
    public static Object[] setObjectsInArray(final Object[] pArray, final String pBeanSetPropertyName, final Object pSetValue) {
        return setObjectsInArray(pArray, null, null, -1, pBeanSetPropertyName, pSetValue);
    }

    /**
     * A utility method that searches through an array of objects and sets a
     * value for all objects that have a property whose value matches the filter
     * value passed in. Also returns an array of those matched values.
     * 
     * NOTE: even though this method returns a filtered array, the objects will
     * be changed in the original array passed in as well.
     * 
     * For example, <blockquote>
     * 
     * <pre>
     * Person personArrayList[] = . . . &lt;font color=&quot;#339933&quot;&gt;//(get an array of populated Person objects from somewhere)&lt;/font&gt;
     * Person returnPersonList[] = Utility.setObjectsInArray(personList,    &lt;font color=&quot;#339933&quot;&gt;// &lt;- The array &lt;/font&gt;
     *                                                       &quot;age&quot;,         &lt;font color=&quot;#339933&quot;&gt;// &lt;- The bean property name to filter on &lt;/font&gt;
     *                                                       &quot;1&quot;,           &lt;font color=&quot;#339933&quot;&gt;// &lt;- The filter value &lt;/font&gt;
     *                                                       3,             &lt;font color=&quot;#339933&quot;&gt;// &lt;- The index in the array from which to start &lt;/font&gt;
     *                                                       &quot;isSelected&quot;,  &lt;font color=&quot;#339933&quot;&gt;// &lt;- The bean property name on which to set a value&lt;/font&gt;
     *                                                       BOOLEAN.TRUE); &lt;font color=&quot;#339933&quot;&gt;// &lt;- The value to set &lt;/font&gt;
     * </pre>
     * 
     * </blockquote> returns all Person objects in the personList (array) where:
     * <code>person.getAge().equals("1")</code>. All objects returned in the
     * array will have <code>person.setIsSelected(BOOLEAN.TRUE)</code>. NOTE:
     * Starts looking from index 3.
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanFilterPropertyName
     *            - The property name to search for.
     * @param pFilterValue
     *            - The value to search for.
     * @param pStartIndex
     *            - the non-negative index to start searching from.
     * @param pBeanSetPropertyName
     * @param pSetValue
     * @return Object[] - returns a sub-set of the original array.
     */
    public static Object[] setObjectsInArray(final Object[] pArray, final String pBeanFilterPropertyName, final Object pFilterValue,
            final int pStartIndex, final String pBeanSetPropertyName, final Object pSetValue)
    {
        final Vector vRetList = new Vector();
        Class vArrayClass = null;
        Object vArrayObject = null;

        int vObjIndex = -1;

        if (pStartIndex > 0) {
            vObjIndex = indexOfArray(pArray, pBeanFilterPropertyName, pFilterValue, pStartIndex);
        }
        else {
            vObjIndex = indexOfArray(pArray, pBeanFilterPropertyName, pFilterValue);
        }

        if (vObjIndex < 0) {
            return null;
        }

        // if BOTH pBeanFilterPropertyName AND pFilterValue are NULL, then set
        // the value
        // on ALL objects.....
        if (Utility.isNullOrEmpty(pBeanFilterPropertyName) && Utility.isNullOrEmpty(pFilterValue)) {
            for (int i = 0; i < pArray.length; i++) {
                ClassUtil.setBeanProperty(pArray[vObjIndex], pBeanSetPropertyName, pSetValue);

                vRetList.add(pArray[vObjIndex]);
            }
        }
        // ....otherwise, set the objects on only the ones that match the
        // filter.
        else {
            while (vObjIndex >= 0) {
                ClassUtil.setBeanProperty(pArray[vObjIndex], pBeanSetPropertyName, pSetValue);

                vRetList.add(pArray[vObjIndex]);
                vObjIndex = indexOfArray(pArray, pBeanFilterPropertyName, pFilterValue, vObjIndex + 1);
            }
        }

        if (vRetList.isEmpty()) {
            return null;
        }

        // Now that we have all of the objects we need (i.e. those that match
        // the filter),
        // create an array and copy the values from the list into the array.
        // Make sure
        // that the Array type returned is the same as the Array type passed in.
        vArrayClass = pArray[0].getClass();
        vArrayObject = Array.newInstance(vArrayClass, vRetList.size());
        vRetList.copyInto((Object[]) vArrayObject);

        return (Object[]) vArrayObject;
    }

    /**
     * A utility method that searches through an array of objects and returns
     * all objects that have a property whose value matches the value passed in.
     * 
     * For example, <blockquote>
     * 
     * <pre>
     * Person personArrayList[] = . . . &lt;font color=&quot;#339933&quot;&gt;//(get an array of populated Person objects from somewhere)&lt;/font&gt;
     * Person returnPersonList[] = Utility.getObjectsFromArray(personList, &quot;age&quot;, &quot;1&quot;, 3);
     * </pre>
     * 
     * </blockquote> returns all Person objects in the personList (array) where:
     * <code>person.getAge().equals"(1")</code>. NOTE: Starts looking from index
     * 3.
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanPropertyName
     *            - The property name to search for.
     * @param pValue
     *            - The value to search for.
     * @param pStartIndex
     *            - the non-negative index to start searching from.
     * @return Object[] - returns a sub-set of the original array.
     */
    public static Object[] getObjectsFromArray(final Object[] pArray, final String pBeanPropertyName, final Object pValue, final int pStartIndex) {
        final Vector vRetList = new Vector();
        Class vArrayClass = null;
        Object vArrayObject = null;

        if (Utility.isNullOrEmpty(pArray)) {
            return null;
        }

        int vObjIndex = -1;

        if (pStartIndex > 0) {
            vObjIndex = indexOfArray(pArray, pBeanPropertyName, pValue, pStartIndex);
        }
        else {
            vObjIndex = indexOfArray(pArray, pBeanPropertyName, pValue);
        }

        if (vObjIndex < 0) {
            return null;
        }

        while (vObjIndex >= 0) {
            vRetList.add(pArray[vObjIndex]);
            vObjIndex = indexOfArray(pArray, pBeanPropertyName, pValue, vObjIndex + 1);
        }

        if (vRetList.isEmpty()) {
            return null;
        }

        // Now that we have all of the objects we need (i.e. those that match
        // the filter),
        // create an array and copy the values from the list into the array.
        // Make sure
        // that the Array type returned is the same as the Array type passed in.
        vArrayClass = pArray[0].getClass();
        vArrayObject = Array.newInstance(vArrayClass, vRetList.size());
        vRetList.copyInto((Object[]) vArrayObject);

        return (Object[]) vArrayObject;
    }

    /**
     * A utility method that searches through an array of objects and returns
     * all objects that have a property whose value matches the value passed in.
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanPropertyName
     *            - The property name to search for.
     * @param pValueArray
     *            - The list of values to search for.
     * @return Object[] - returns a sub-set of the original array.
     */
    public static Object[] getObjectsFromArray(final Object[] pArray, final String pBeanPropertyName, final Object[] pValueArray) {
        return getObjectsFromArray(pArray, pBeanPropertyName, pValueArray, -1);
    }

    /**
     * A utility method that searches through an array of objects and returns
     * all objects that have a property whose value matches the value passed in.
     * 
     * @param pArray
     *            - The array of objects to search for.
     * @param pBeanPropertyName
     *            - The property name to search for.
     * @param pValueArray
     *            - The list of values to search for.
     * @param pStartIndex
     *            - the non-negative index to start searching from.
     * @return Object[] - returns a sub-set of the original array.
     */
    public static Object[] getObjectsFromArray(final Object[] pArray, final String pBeanPropertyName, final Object[] pValueArray,
            final int pStartIndex)
    {
        final Vector vRetList = new Vector();
        Object[] vCurrArray = null;
        Object vValueObj = null;
        Class vArrayClass = null;
        Object vArrayObject = null;

        if (pArray == null || pBeanPropertyName == null || pValueArray == null) {
            return null;
        }

        for (int i = 0; i < pValueArray.length; i++) {
            vValueObj = pValueArray[i];
            vCurrArray = getObjectsFromArray(pArray, pBeanPropertyName, vValueObj, pStartIndex);

            if (vCurrArray != null && vCurrArray.length > 0) {
                vRetList.addAll(Arrays.asList(vCurrArray));
            }
        }

        if (vRetList.isEmpty()) {
            return null;
        }

        // Now that we have all of the objects we need (i.e. those that match
        // the filter),
        // create an array and copy the values from the list into the array.
        // Make sure
        // that the Array type returned is the same as the Array type passed in.
        vArrayClass = pArray[0].getClass();
        vArrayObject = Array.newInstance(vArrayClass, vRetList.size());
        vRetList.copyInto((Object[]) vArrayObject);

        return (Object[]) vArrayObject;
    }

    /**
     * A utility method that extracts properties and
     * 
     * @param pArray
     *            Object[]
     * @param pBeanPropertyName
     *            String
     * @return Object[]
     */
    public static Object[] extractObjectArray(final Object[] pArray, final String pBeanPropertyName) {
        Object vArrayObj = null;
        Object vArrayValueObj = null;
        final Vector vRetList = new Vector();
        Class vArrayClass = null;
        Object vArrayObject = null;

        if (pArray == null || pBeanPropertyName == null) {
            return null;
        }

        for (int i = 0; i < pArray.length; i++) {
            vArrayObj = pArray[i];
            vArrayValueObj = ClassUtil.getBeanPropertyString(vArrayObj, pBeanPropertyName);
            if (vArrayValueObj != null) {
                vArrayClass = vArrayValueObj.getClass();
                vRetList.add(vArrayValueObj);
            }
        }

        if (vRetList.isEmpty()) {
            return null;
        }

        // Now that we have all of the objects we need (i.e. those extracted),
        // create an array and copy the values from the list into the array.
        // Make sure
        // that the Array type returned is the same as the Array type passed in.
        vArrayObject = Array.newInstance(vArrayClass, vRetList.size());
        vRetList.copyInto((Object[]) vArrayObject);

        return (Object[]) vArrayObject;

    }

    /**
     * loop through each object, calling the toString of the object, and separte
     * each object by by new line
     * 
     * @example: GuestDtoTest[] guests //has something
     *           Utility.toStringFromArray(guests);
     * @param dtoLst
     *            Object[] contains a list of objects
     * @return String
     */
    public static String toStringFromArray(final Object[] dtoLst) {
        final StringBuffer sb = new StringBuffer();
        if (dtoLst == null) {
            return sb.toString();
        }
        for (int lop = 0; lop < dtoLst.length; lop++) {
            if (dtoLst[lop] != null) {
                sb.append((lop > 0 ? "\n\t" : "") + dtoLst[lop].toString());
            }
        }
        return sb.toString();
    }

    public static List removeDuplicatesFromList(final List pList) {
        List vRetList = null;
        Object vObj = null;

        try {
            vRetList = pList.getClass().newInstance();
        }
        catch (final InstantiationException e) {
            return null;
        }
        catch (final IllegalAccessException e) {
            return null;
        }

        for (int i = 0; i < pList.size(); i++) {
            vObj = pList.get(i);

            if (!vRetList.contains(vObj)) {
                vRetList.add(vObj);
            }
        }

        return vRetList;
    }

    public static List removeDuplicatesFromList(final List pList, final String pBeanPropertyName) {
        List vRetList = null;
        Object vObj = null;

        if (pList == null || pList.isEmpty()) {
            return pList;
        }

        try {
            vRetList = pList.getClass().newInstance();
        }
        catch (final InstantiationException e) {
            return null;
        }
        catch (final IllegalAccessException e) {
            return null;
        }

        for (int i = 0; i < pList.size(); i++) {
            vObj = pList.get(i);

            if (!listContainsField(vRetList, pBeanPropertyName, vObj)) {
                vRetList.add(vObj);
            }
        }

        return vRetList;
    }

    public static boolean listContainsAtLeastOneInList(final List pList, final String pBeanPropertyName, final List pValueList) {
        Object vListObj = null;

        if (pList == null) {
            return false;
        }

        for (int i = 0; i < pList.size(); i++) {
            vListObj = pList.get(i);
            if (listContainsField(pValueList, pBeanPropertyName, vListObj)) {
                return true;
            }
        }

        return false;
    }

    public static void populateArrayObjectsWithValue(final Object[] array, final String beanPropertyName, final Object value) {
        populateArrayObjectsWithValue(array, null, beanPropertyName, value);
    }

    public static void populateArrayObjectsWithValue(final Object[] array, final int[] indexNumbers, final String beanPropertyName,
            final Object value)
    {
        Object obj = null;
        if (Utility.isNullOrEmpty(array) || Utility.isNullOrEmpty(beanPropertyName) || Utility.isNullOrEmpty(value)) {
            return;
        }

        if (!Utility.isNullOrEmpty(indexNumbers)) {
            Arrays.sort(indexNumbers);
        }

        for (int i = 0; i < array.length; i++) {
            if (Utility.isNullOrEmpty(indexNumbers) || Arrays.binarySearch(indexNumbers, i) >= 0) {
                obj = array[i];
                ClassUtil.setBeanProperty(obj, beanPropertyName, value);
            }
        }
    }

    public static void populateListObjectsWithValue(final List list, final String beanPropertyName, final Object value) {
        Object obj = null;
        if (Utility.isNullOrEmpty(list) || Utility.isNullOrEmpty(beanPropertyName) || Utility.isNullOrEmpty(value)) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            obj = list.get(i);
            ClassUtil.setBeanProperty(obj, beanPropertyName, value);
        }
    }

    /**
     * This method is an extension of the java.util.List.contains(Object o)
     * method. Instead of checking to see if the list contains an object, it
     * allows you to test if the list contains an object that has a certain
     * property value.
     * 
     * Formally, the test is as such:
     * <tt>(o.get<pBeanPropertyName>.equals(e.get<pBeanPropertyName>))</tt>
     * 
     * @param pList
     *            - The list that you want to search
     * @param pBeanPropertyName
     *            - The property name you want to test on the beans in the list.
     *            NOTE: the BeanPropertyName conforms to the JavaBean
     *            specification. So, if you want to test that a bean's
     *            getLastName() method has a certain value, pass in "lastName".
     * @param pValue
     *            - The value you want to search for.
     * @return
     * @throws BusinessLogicException
     *             if there is an exception trying to get BeanPropertyName from
     *             the beans in the list. i.e. the property name doesn't exist
     *             on one of the bean.
     */
    public static boolean listContainsField(final List pList, final String pBeanPropertyName, final Object pValue) {
        return listContainsField(pList, pBeanPropertyName, pValue, false);
    }

    /**
     * This method is an extension of the java.util.List.contains(Object o)
     * method. Instead of checking to see if the list contains an object, it
     * allows you to test if the list contains an object that has a certain
     * property value.
     * 
     * Formally, the test is as such:
     * <tt>(o.get<pBeanPropertyName>.equals(e.get<pBeanPropertyName>))</tt>
     * 
     * @param pList
     *            - The list that you want to search
     * @param pBeanPropertyName
     *            - The property name you want to test on the beans in the list.
     *            NOTE: the BeanPropertyName conforms to the JavaBean
     *            specification. So, if you want to test that a bean's
     *            getLastName() method has a certain value, pass in "lastName".
     * @param pValue
     *            - The value you want to search for.
     * @return
     * @throws BusinessLogicException
     *             if there is an exception trying to get BeanPropertyName from
     *             the beans in the list. i.e. the property name doesn't exist
     *             on one of the bean.
     */
    public static boolean listContainsField(final List pList, final String pBeanPropertyName, final Object pValue, final boolean pUseValueItself) {
        Object vListObj = null;
        Object vListValueObj = null;
        Object vValueObj = null;

        if (pList == null || pList.isEmpty() || pBeanPropertyName == null || "".equals(pBeanPropertyName.trim())
                || pValue == null)
        {
            return false;
        }

        for (int i = 0; i < pList.size(); i++) {
            vListObj = pList.get(i);
            vListValueObj = ClassUtil.getBeanPropertyString(vListObj, pBeanPropertyName);
            if (!pUseValueItself) {
                vValueObj = ClassUtil.getBeanPropertyString(pValue, pBeanPropertyName);
            }
            else {
                vValueObj = pValue;
            }

            if (vValueObj != null && vValueObj.equals(vListValueObj)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method is an extension of the java.util.List.contains(Object o)
     * method. Instead of checking to see if the list contains an object, it
     * allows you to test if the list contains an object that has a certain
     * property value.
     * 
     * Formally, the test is as such:
     * <tt>(o.get<pBeanPropertyName>.equals(e.get<pBeanPropertyName>))</tt>
     * 
     * @param pList
     *            - The list that you want to search
     * @param pBeanPropertyName
     *            - The property name you want to test on the beans in the list.
     *            NOTE: the BeanPropertyName conforms to the JavaBean
     *            specification. So, if you want to test that a bean's
     *            getLastName() method has a certain value, pass in "lastName".
     * @param pValue
     *            - The value you want to search for.
     * @return
     * @throws BusinessLogicException
     *             if there is an exception trying to get BeanPropertyName from
     *             the beans in the list. i.e. the property name doesn't exist
     *             on one of the bean.
     */
    public static boolean arrayContainsField(final Object[] pArray, final String pBeanPropertyName, final Object pValue) {
        return arrayContainsField(pArray, pBeanPropertyName, pValue, false);
    }

    /**
     * This method is an extension of the java.util.List.contains(Object o)
     * method. Instead of checking to see if the list contains an object, it
     * allows you to test if the list contains an object that has a certain
     * property value.
     * 
     * Formally, the test is as such:
     * <tt>(o.get<pBeanPropertyName>.equals(e.get<pBeanPropertyName>))</tt>
     * 
     * @param pArray
     *            - The list that you want to search
     * @param pBeanPropertyName
     *            - The property name you want to test on the beans in the list.
     *            NOTE: the BeanPropertyName conforms to the JavaBean
     *            specification. So, if you want to test that a bean's
     *            getLastName() method has a certain value, pass in "lastName".
     * @param pValue
     *            - The value you want to search for.
     * @return
     * @throws BusinessLogicException
     *             if there is an exception trying to get BeanPropertyName from
     *             the beans in the list. i.e. the property name doesn't exist
     *             on one of the bean.
     */
    public static boolean arrayContainsField(final Object[] pArray, final String pBeanPropertyName, final Object pValue,
            final boolean pUseValueItself)
    {
        Object vListObj = null;
        Object vListValueObj = null;
        Object vValueObj = null;

        if (Utility.isNullOrEmpty(pArray) || Utility.isNullOrEmpty(pBeanPropertyName) || Utility.isNullOrEmpty(pValue)) {
            return false;
        }

        for (int i = 0; i < pArray.length; i++) {
            vListObj = pArray[i];
            vListValueObj = ClassUtil.getBeanPropertyString(vListObj, pBeanPropertyName);
            if (!pUseValueItself) {
                vValueObj = ClassUtil.getBeanPropertyString(pValue, pBeanPropertyName);
            }
            else {
                vValueObj = pValue;
            }

            if (vValueObj != null && vValueObj.equals(vListValueObj)) {
                return true;
            }
        }

        return false;
    }

    public static Object getObjectFromList(final Collection pList, final String pBeanPropertyName, final Object pValue) {
        List returnList = null;

        returnList = getObjectsFromList(pList, pBeanPropertyName, pValue);

        if (Utility.isNullOrEmpty(returnList)) {
            return null;
        }

        return returnList.get(0);
    }

    public static List getObjectsFromList(final Collection pList, final String pBeanPropertyName, final Object pValue) {
        final ArrayList returnList = new ArrayList();
        final int vObjIndex = -1;
        Object vListValueObj = null;

        if (pList == null || pList.isEmpty() || pBeanPropertyName == null || "".equals(pBeanPropertyName.trim())
                || pValue == null)
        {
            throw new IllegalArgumentException("List and/or pBeanPropertyName and/or Value "
                    + "is null.  All values must be supplied.");
        }

        for (final Object vListObj : pList) {
            vListValueObj = ClassUtil.getBeanPropertyString(vListObj, pBeanPropertyName);

            if (vListValueObj != null && vListValueObj.equals(pValue)) {
                returnList.add(vListObj);
            }
        }

        return returnList;
    }

    public static Object[] removeDuplicatesFromArray(final Object[] objArray) {
        final Vector vRetList = new Vector();
        Object obj = null;
        Class vArrayClass = null;
        Object vArrayObject = null;

        if (Utility.isNullOrEmpty(objArray)) {
            return objArray;
        }

        for (int i = 0; i < objArray.length; i++) {
            obj = objArray[i];

            if (!vRetList.contains(obj)) {
                vRetList.add(obj);
            }
        }

        // Now that we have all of the objects we need (i.e. those that match
        // the filter),
        // create an array and copy the values from the list into the array.
        // Make sure
        // that the Array type returned is the same as the Array type passed in.
        vArrayClass = objArray[0].getClass();
        vArrayObject = Array.newInstance(vArrayClass, vRetList.size());
        vRetList.copyInto((Object[]) vArrayObject);

        return (Object[]) vArrayObject;
    }

}
