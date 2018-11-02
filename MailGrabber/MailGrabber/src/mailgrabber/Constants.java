/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mailgrabber;

/**
 *
 * @author Ciprian
 */
public class Constants {
    
    public static final String PHONE_REG_EXP = "(\\+4)?(0)[2|3|7][0-9]+[\\.\\s]?[0-9]+[\\.\\s]?[0-9]+[\\.\\s]?[0-9]+[\\.\\s]?[0-9]+[\\.\\s]?[0-9]+[\\.\\s]?";
    public static final String EMAIL_REG_EXP = "([a-z0-9_\\.-])+@(([a-z0-9-])+\\.)+([a-z0-9]{2,4})+";
    public static final String URL_REG_EXP = "(href=\\s*[\"|\']\\s*)((http://)[a-z_0-9\\-]+(\\.\\w[a-z_0-9\\-]+)+(/[#&;\\.\\n\\-=?\\+\\%/\\.\\w]+)?)";
    public static final String DOMAIN_REG_EXP = "(http://)([a-z_0-9\\-]+(\\.\\w[a-z_0-9\\-]+)(\\.\\w[a-z]+))";
}
