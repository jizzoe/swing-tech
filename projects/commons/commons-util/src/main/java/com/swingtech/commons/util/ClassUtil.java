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

import java.beans.BeanInfo;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DOCME
 *
 * @author Joe Rice
 *
 */
public class ClassUtil {
    private final static Log logger = LogFactory.getLog(ClassUtil.class.getName());

    public ClassUtil() {
        super();
    }

    public static Object invokeMethod(final Object object, final String methodName, final Object[] params) throws NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Method method = null;

        if (object == null) {
            return null;
        }

        if (Utility.isNullOrEmpty(methodName)) {
        }

        method = object.getClass().getMethod(methodName, getClassArrayFromObjectArray(params));

        return method.invoke(object, params);
    }

    public static String getClassNameFromFullPath(final Class pClazz) {
        if (pClazz == null) {
            throw new RuntimeException(
                    "Could not get ClassNameFromFullPath.  pClazz passed in is NULL.  Cannot be null.");
        }

        return getClassNameFromFullPath(pClazz.getName());
    }

    public static String getClassNameFromFullPath(final String pClassName) {
        int vDotIndex = -1;

        vDotIndex = pClassName.lastIndexOf(".");

        if (vDotIndex < 0) {
            return pClassName;
        }

        return pClassName.substring(vDotIndex + 1);
    }

    public static Class getPropertyType(final Class clazz, final String propertyName) {
        PropertyDescriptor[] propertyDescriptorList = null;
        PropertyDescriptor propertyDescriptor = null;

        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        }

        propertyDescriptorList = PropertyUtils.getPropertyDescriptors(clazz);

        for (int i = 0; i < propertyDescriptorList.length; i++) {
            propertyDescriptor = propertyDescriptorList[i];

            if (propertyDescriptor.getName().equalsIgnoreCase(propertyName)) {
                return propertyDescriptor.getPropertyType();
            }
        }

        return null;
    }

    public static List getPropertyNamesFromClass(final Object pObject) {
        return getPropertyNamesFromClass(pObject.getClass());
    }

    public static List getPropertyNamesFromClass(final Class pClass) {
        return getPropertyNamesFromClass(pClass, null);
    }

    /**
     * @param pClass
     * @param filterRegEx
     * @return
     */
    public static List getPropertyNamesFromClass(final Class pClass, final String filterRegEx) {
        BeanInfo vBeanInfo = null;
        PropertyDescriptor vPropDescList[] = null;
        PropertyDescriptor vPropDesc = null;
        String vPropName = null;
        final Vector vPropertyNameList = new Vector();

        try {
            vBeanInfo = Introspector.getBeanInfo(pClass);
        }
        catch (final IntrospectionException e) {
            logger.warn("Utility.getPropertyNamesFromClass.  Error trying to use Introspector", e);
            return null;
        }

        vPropDescList = vBeanInfo.getPropertyDescriptors();

        for (int i = 0; i < vPropDescList.length; i++) {
            vPropDesc = vPropDescList[i];

            vPropName = vPropDesc.getName();

            if (Utility.isNullOrEmpty(filterRegEx) || vPropName.matches(filterRegEx)) {
                vPropertyNameList.add(vPropName);
            }
        }

        if (vPropertyNameList.isEmpty()) {
            return null;
        }

        return vPropertyNameList;
    }

    public static List getPropertyDescriptorsFromClass(final Object pObject) {
        return getPropertyDescriptorsFromClass(pObject.getClass());
    }

    public static List getPropertyDescriptorsFromClass(final Class pClass) {
        return getPropertyDescriptorsFromClass(pClass, null);
    }

    public static List getPropertyDescriptorsFromClass(final Class pClass, final String filterRegEx) {
        BeanInfo vBeanInfo = null;
        PropertyDescriptor[] vPropDescList = null;
        PropertyDescriptor vPropDesc = null;
        String vPropName = null;
        final Vector vPropertyNameList = new Vector();

        try {
            vBeanInfo = Introspector.getBeanInfo(pClass);
        }
        catch (final IntrospectionException e) {
            logger.warn("Utility.getPropertyNamesFromClass.  Error trying to use Introspector", e);
            return null;
        }

        vPropDescList = vBeanInfo.getPropertyDescriptors();

        for (int i = 0; i < vPropDescList.length; i++) {
            vPropDesc = vPropDescList[i];

            vPropName = vPropDesc.getName();

            if (Utility.isNullOrEmpty(filterRegEx) || vPropName.matches(filterRegEx)) {
                vPropertyNameList.add(vPropDesc);
            }
        }

        if (vPropertyNameList.isEmpty()) {
            return null;
        }

        return vPropertyNameList;
    }

    /**
     * Utility method that uses bean introspection to get a String value for a
     * property for any given Bean. This allows you to dynamically get bean
     * values by specifying the the name of the property. This method will
     * return a String.
     *
     * For example: <blockquote>
     *
     * <pre>
     * Person  person = . . . &lt;font color=&quot;#339933&quot;&gt;//(get a populated Person object from somewhere)&lt;/font&gt;
     * String  age = getBeanProperty(person, &quot;age&quot;);
     * </pre>
     *
     * </blockquote> Is the same as: <blockquote>
     *
     * <pre>
     * String age = person.getAge();
     * </pre>
     *
     * </blockquote>
     *
     * @Deprecated This method returns a String, instead of an object. Might
     *             have some wierd unexpected behavior. Use getBeanProperty()
     *             instead. It returns an object.
     *
     * @param pBean
     *            - the bean which contains the data for which you want to get.
     * @param pPropertyName
     *            - the bean property you want to get the data for. This should
     *            follow all the JavaBean convention...i.e. if the property name
     *            is "age", there should be a method "getAge()" on the bean.
     * @return Object - The value for the property on the bean.
     *
     */
    public static Object getBeanPropertyString(final Object pBean, final String pPropertyName) {
        Object vReturnObject = null;

        try {
            vReturnObject = BeanUtils.getProperty(pBean, pPropertyName);
        }
        catch (final Exception e) {
            throw new RuntimeException("Error occured trying to get property " + "name, '" + pPropertyName
                    + "', from object of class, '" + pBean.getClass().getName() + "'.  Error:  "
                    + e.getClass().getName() + ":  " + e.getMessage(), e);
        }

        return vReturnObject;
    }

    /**
     * Utility method that uses bean introspection to get a value for a property
     * for any given Bean. This allows you to dynamically get bean values by
     * specifying the the name of the property. This method will return an
     * object.
     *
     * For example: <blockquote>
     *
     * <pre>
     * Person  person = . . . &lt;font color=&quot;#339933&quot;&gt;//(get a populated Person object from somewhere)&lt;/font&gt;
     * String  age = getBeanProperty(person, &quot;age&quot;);
     * </pre>
     *
     * </blockquote> Is the same as: <blockquote>
     *
     * <pre>
     * String age = person.getAge();
     * </pre>
     *
     * </blockquote>
     *
     * @param pBean
     *            - the bean which contains the data for which you want to get.
     * @param pPropertyName
     *            - the bean property you want to get the data for. This should
     *            follow all the JavaBean convention...i.e. if the property name
     *            is "age", there should be a method "getAge()" on the bean.
     * @return Object - The value for the property on the bean.
     */
    public static Object getBeanProperty(final Object pBean, final String pPropertyName) {
        Object vReturnObject = null;

        try {
            vReturnObject = PropertyUtils.getProperty(pBean, pPropertyName);
        }
        catch (final RuntimeException re) {
            // if it's a runtime exception, throw the original exception. This
            // way, it gets bubbled to the top.
            throw re;
        }
        catch (final Exception e) {
            throw new RuntimeException("Error occured trying to get property " + "name, '" + pPropertyName
                    + "', from object of class, '" + pBean.getClass().getName() + "'.  Error:  "
                    + e.getClass().getName() + ":  " + e.getMessage(), e);
        }

        return vReturnObject;
    }

    /**
     * Utility method that uses bean introspection to set a value for a property
     * for any given Bean. This allows you to dynamically set bean values by
     * specifying the the name of the property and a value
     *
     * For example: <blockquote>
     *
     * <pre>
     * Person  person = . . . &lt;font color=&quot;#339933&quot;&gt;//(get a populated Person object from somewhere)&lt;/font&gt;
     * String  age = setBeanProperty(person, &quot;age&quot;, &quot;23&quot;);
     * </pre>
     *
     * </blockquote> Is the same as: <blockquote>
     *
     * <pre>
     * String age = person.setAge(&quot;23&quot;);
     * </pre>
     *
     * </blockquote>
     *
     * @param pBean
     *            - the bean which contains the data for which you want to get.
     * @param pPropertyName
     *            - the bean property you want to set the data for. This should
     *            follow all the JavaBean convention...i.e. if the property name
     *            is "age", there should be a method "getAge()" on the bean.
     * @param pValue
     *            - The value you want to set for the bean.
     * @throws RuntimeException
     *             - If the propertyName is not found on the bean or if the
     *             value passed in is not the expected data type or object type
     *             expected.
     * @return Object - The value for the property on the bean.
     */
    public static void setBeanProperty(final Object pBean, final String pPropertyName, final Object pValue) {
        try {
            BeanUtils.setProperty(pBean, pPropertyName, pValue);
        }
        catch (final Exception e) {
            throw new RuntimeException("Error occured trying to set property " + "name, '" + pPropertyName
                    + "', in object of class, '" + pBean.getClass().getName() + "'.  Error:  "
                    + e.getClass().getName() + ":  " + e.getMessage(), e);
        }

        return;
    }

    public static Object instantiateObjectSameType(final Object inObj, final Object[] args) {
        return instantiateObject(inObj.getClass(), args);
    }

    public static Object instantiateObjectSameType(final Class inClass, final Object[] args) {
        return instantiateObject(inClass, args);
    }

    public static boolean beanContainsPropertyValue(final Object bean, final String beanPropertyName, final Object value) {
        Object beanValue;

        try {
            beanValue = getBeanProperty(bean, beanPropertyName);
        }
        catch (final Throwable t) {
            return false;
        }

        return beanValue.equals(value);
    }

    public static boolean beanContainsPropertyValue(final Object bean, final List beanPropertyNames, final List values, final boolean matchAll) {
        boolean containsProperty = false;
        String beanPropertyName = null;
        Object value = null;

        if (Utility.isNullOrEmpty(bean) || Utility.isNullOrEmpty(beanPropertyNames) || Utility.isNullOrEmpty(values)) {
            throw new IllegalArgumentException("Either bean or beanPropertyNames or values is null.  "
                    + "All of these fields must be present.");
        }

        if (beanPropertyNames.size() != values.size()) {
            throw new IllegalArgumentException("beanPropertyNames list does not contain the same number of "
                    + "elements as values list.  These lists must be same size.");
        }

        for (int i = 0; i < beanPropertyNames.size(); i++) {
            beanPropertyName = (String) beanPropertyNames.get(i);
            value = values.get(i);

            if (beanContainsPropertyValue(bean, beanPropertyName, value)) {
                containsProperty = true;

                // if andCondition == false (i.e. only needs 1 of the regEx's to
                // match), then the condition is
                // met, so break out of the loop and return true.
                if (!matchAll) {
                    break;
                }
            }
            else {
                if (matchAll) {
                    // if andCondition == true (i.e. ALL of the regEx's have to
                    // match), then if one doesn't match,
                    // break out of the loop and return false;
                    containsProperty = false;
                    break;
                }
            }
        }

        return containsProperty;
    }

    public static Object instantiateObject(final Class inClass, final Object[] args) {
        Constructor constructor = null;

        if (inClass == null) {
            throw new IllegalArgumentException("inClass can not be null.");
        }

        try {
            constructor = inClass.getDeclaredConstructor(getClassArrayFromObjectArray(args));

            return constructor.newInstance(args);
        }
        catch (final Exception e) {
            throw new RuntimeException("Error trying to instatiate class, '" + inClass.getName() + "'.  Error:  "
                    + e.getClass().getName() + ":  " + e.getMessage() + ".");
        }
    }

    public static Class[] getClassArrayFromObjectArray(final Object[] objArray) {
        Class[] classArray = null;

        if (Utility.isNullOrEmpty(objArray)) {
            return null;
        }

        classArray = new Class[objArray.length];

        for (int i = 0; i < objArray.length; i++) {
            classArray[i] = objArray[i].getClass();
            ;
        }

        return classArray;
    }

    /**
     * NOTE: When using this to print an object it will not display the
     * primitive type boolean. Must use the wrapper class. All other primitives
     * work fine.
     *
     * NOTE: If an int value has a 0 it won't display.
     *
     * NOTE: Object must have a public constructor.
     *
     * @param object
     * @return
     */
    public static String getXMLForObject(final Object object) {
        ByteArrayOutputStream baos = null;
        XMLEncoder e = null;

        baos = new ByteArrayOutputStream();
        e = new XMLEncoder(new BufferedOutputStream(baos));

        e.setPersistenceDelegate(Date.class, new PersistenceDelegate() {
            @Override
            protected Expression instantiate(final Object oldInstance, final Encoder out) {
                final Date date = (Date) oldInstance;
                final Long time = new Long(date.getTime());
                return new Expression(date, date.getClass(), "new", new Object[]
                    { time });
            }
        });

        e.setPersistenceDelegate(BigDecimal.class, new PersistenceDelegate() {
            @Override
            protected Expression instantiate(final Object oldInstance, final Encoder out) {
                final BigDecimal bigDec = (BigDecimal) oldInstance;
                final double doubleVal = bigDec.doubleValue();
                return new Expression(bigDec, bigDec.getClass(), "new", new Object[]
                    { new Double(doubleVal) });
            }
        });

        e.writeObject(object);
        e.close();

        return baos.toString();
    }

    public static Object getObjectFromXML(final File file) throws FileNotFoundException {
        final FileInputStream inStream = new FileInputStream(file);
        return getObjectFromXML(inStream);
    }

    public static Object getObjectFromXML(final URL url) throws IOException {
        return getObjectFromXML(url.openStream());
    }

    public static Object getObjectFromXML(final InputStream inStream) {
        final XMLDecoder d = new XMLDecoder(new BufferedInputStream(inStream));

        final Object result = d.readObject();
        d.close();
        return result;
    }

    public static byte[] serializeObject(final Object object) {
        ObjectOutput out = null;
        byte[] buf = null;

        if (object == null) {
            return null;
        }

        try {
            // Serialize to a byte array
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.close();

            // Get the bytes of the serialized object
            buf = bos.toByteArray();
        }
        catch (final Exception e) {
            throw new RuntimeException("Error trying to serializeObject the following object:  " + object, e);
        }

        return buf;
    }

    public static Object deserializeObject(final byte[] bytes) {
        ObjectInputStream in = null;
        Object obj = null;

        if (bytes == null) {
            return null;
        }

        try {
            in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            obj = in.readObject();
            in.close();
        }
        catch (final Exception e) {
            throw new RuntimeException("Error trying to deserializeObject from this byte array:  " + bytes, e);
        }

        return obj;
    }

    public static String getMethodDeclarationString(final Method method) {
        final StringBuffer retStringBuff = new StringBuffer();

        retStringBuff.append(ClassUtil.getClassNameFromFullPath(method.getReturnType()));
        retStringBuff.append(" ");
        retStringBuff.append(ClassUtil.getClassNameFromFullPath(method.getDeclaringClass()));
        retStringBuff.append(".");
        retStringBuff.append(method.getName());
        retStringBuff.append("(");

        if (!Utility.isNullOrEmpty(method.getParameterTypes())) {
            Class c = null;
            for (int index = 0; index < method.getParameterTypes().length; index++) {
                c = method.getParameterTypes()[0];
                if (index > 1) {
                    retStringBuff.append(", ");
                }
                retStringBuff.append(ClassUtil.getClassNameFromFullPath(c));
                retStringBuff.append(" arg" + index);
            }
        }

        retStringBuff.append(")");

        return retStringBuff.toString();
    }
}
