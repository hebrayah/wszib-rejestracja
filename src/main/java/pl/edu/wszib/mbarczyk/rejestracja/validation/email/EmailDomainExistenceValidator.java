package pl.edu.wszib.mbarczyk.rejestracja.validation.email;

import lombok.extern.slf4j.Slf4j;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;

@Slf4j
public class EmailDomainExistenceValidator implements Consumer<String> {

    private final Hashtable<String, String> env;

    public EmailDomainExistenceValidator(String dnsServerUrl) {
        env = new Hashtable<>();
        env.put(INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        env.put(PROVIDER_URL, dnsServerUrl);
    }

    @Override
    public void accept(String email) {
      log.info("Checking email: {}", email);
      String domain = email.substring(email.indexOf("@") + 1);
      log.debug("Domain: {}", domain);
      try {
          getMX(domain)
                  .stream()
                  .findAny()
                  .orElseThrow(()->new EmailValidationException("Brak serwera pocztowego"));
      } catch (NamingException ne) {
          throw new EmailValidationException("Niepoprawny adres serwera pocztowego", ne);
      }
    }

    private List<String> getMX(String hostName) throws NamingException {
        DirContext ctx =  new InitialDirContext(env);

        Attributes attrs = ctx.getAttributes(hostName,  new String[]{"MX"});
        Attribute attr = attrs.get("MX");
        log.debug("MX: {}", attr);
        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attrs = ctx.getAttributes(hostName, new String[]{"A"});
            attr = attrs.get("A");
            log.debug("A: {}", attr);
            if (attr == null)
                throw new NamingException("No match for name '" + hostName + "'");
        }
        List<String> res = new ArrayList<>();
        NamingEnumeration<?> en = attr.getAll();
        while (en.hasMore()) {
            String mailhost;
            String x = (String) en.next();
            String f[] = x.split(" ");
            // THE fix *************
            if (f.length == 1)
                mailhost = f[0];
            else if (f[1].endsWith("."))
                mailhost = f[1].substring(0, (f[1].length() - 1));
            else
                mailhost = f[1];
            // THE fix *************
            res.add(mailhost);
        }
        log.info("Email servers: {}", res);
        return res;
    }
}
