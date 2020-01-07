package org.nh.core.util.ldap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.SortControl;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @class: LDAPAuthenticationUtil
 * @description: TODO LDAP连接类
 * @date 2020/1/7 4:18 PM
**/
@Component
@Slf4j
public class LDAPAuthenticationUtil {
    private final static String URL = "ldap://10.0.3.22:389/";
    private final static String BASEDN = "DC=gome,DC=inc"; // 根据自己情况进行修改
    //初始化工厂
    private final static String FACTORY_INITIAL = "com.sun.jndi.ldap.LdapCtxFactory";
    private final static String FACTORY_OBJECT="org.springframework.ldap.core.support.DefaultDirObjectFactory";
    private final static Control[] connCtls = null;


    private LdapContext connect() {
        LdapContext ctx = null;
        Hashtable<String, String> env = new Hashtable<String, String>();
        //java.naming.factory.initial
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY_INITIAL);
        //java.naming.provider.url
        env.put(Context.PROVIDER_URL, URL + BASEDN);
        //java.naming.security.authentication
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        env.put("org.springframework.ldap.base.path", BASEDN);
        env.put("java.naming.factory.object", FACTORY_OBJECT);
        try {
            ctx = new InitialLdapContext(env, connCtls);
            log.info("OaLdapAuth:连接成功");
            System.out.println("OaLdapAuth:连接成功");
            return ctx;
        } catch (AuthenticationException e) {
            log.info("OaLdapAuth:连接失败：");
            System.out.println("OaLdapAuth:连接失败：");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            log.info("OaLdapAuth:连接出错：");
            System.out.println("OaLdapAuth:连接出错：");
            e.printStackTrace();
            return null;
        }

    }

    private static void closeContext(LdapContext ctx) {
        if (ctx != null) {
            try {
                ctx.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }

        }
    }

    public Map<String, Object> authenricate(String userName,String password) {
        LdapContext ctx = connect();


        try {
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, "gome\\" + userName);
//			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userName);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            ctx.reconnect(connCtls);

            log.info(userName + " 验证通过");
            System.out.println(userName + " 验证通过");
            return searchUserFromOA(userName,ctx);
        } catch (AuthenticationException e) {
            log.info(userName + " 验证失败");
            System.out.println(userName + " 验证失败");
            System.out.println(e.toString());
            return null;
        } catch (NamingException e) {
            log.info(userName + " 验证通过");
            System.out.println(userName + " 验证失败");
            return null;
        } finally {
            closeContext(ctx);
        }
    }

    public Map<String, Object> searchUserFromOA(String uid,LdapContext ctx) {

        Map<String, Object> props = new HashMap<String, Object>();

        Control[] sortConnCtls = new SortControl[1];
        NamingEnumeration<SearchResult> enums = null;
        try {
            sortConnCtls[0] = new SortControl("sAMAccountName", Control.CRITICAL);
            ctx.setRequestControls(sortConnCtls);
            String filter = "(sAMAccountName=" + uid + ")";
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            enums = ctx.search("", filter, controls);
            while (enums.hasMoreElements()) {
                SearchResult searchResult = enums.nextElement();
                System.out.println("-------------------------------------------------------------");
                log.info("-------------------------------------------------------------");
                log.info(searchResult.getName());
                System.out.println(searchResult.getName());
                Attributes attributes=searchResult.getAttributes();
                System.out.println(attributes);
                NamingEnumeration<? extends Attribute> a = attributes.getAll();
                while (a.hasMoreElements()) {
                    Attribute attribute = a.nextElement();
                    props.put(attribute.getID(),attribute.get());
                }
                log.info("-------------------------------------------------------------");
                System.out.println("-------------------------------------------------------------");
                return props;
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
