package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchB3Parser extends DispatchB2Parser {

  private String prefix = null;
  private Pattern prefixPattern = null;

  public DispatchB3Parser(String prefix, String[] cityList, String defCity, String defState) {
    this(prefix, cityList, defCity, defState, 0);
  }

  public DispatchB3Parser(String prefix, String defCity, String defState, int flags) {
    this(prefix, null, defCity, defState, flags);
  }

  public DispatchB3Parser(String[] cityList, String defCity, String defState, int flags) {
    this(null, cityList, defCity, defState, flags);
  }

  public DispatchB3Parser(String prefix, String[] cityList, String defCity, String defState, int flags) {
    super(cityList, defCity, defState, flags);
    setupCallList((CodeSet)null);
    this.prefix = prefix;
  }

  public DispatchB3Parser(Pattern prefixPattern, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setupCallList((CodeSet)null);
    this.prefixPattern = prefixPattern;
  }

  public DispatchB3Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setupCallList((CodeSet)null);
  }

  public DispatchB3Parser(String prefix, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setupCallList((CodeSet)null);
    this.prefix = prefix;
  }

  public DispatchB3Parser(Pattern prefixPattern, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setupCallList((CodeSet)null);
    this.prefixPattern = prefixPattern;
  }

  public DispatchB3Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setupCallList((CodeSet)null);
  }

  @Override
 protected boolean parseMsg(String subject, String body, Data data) {
    String tmp;
    if ((tmp = checkPrefix(body)) != null) {
      body = tmp;
    } else if ((tmp = checkPrefix(subject)) != null) {
      subject = tmp;
    } else {
      return false;
    }

    boolean v3 = false;
    if (subject.length() > 0) {
      if (body.startsWith("= DSP")) {
        body = '(' + subject + ") " + body;
      } else if (subject.equals("Return Phone")) {
        body = subject + ": " + body;
      } else if (!subject.startsWith("EVENT:")) {
        v3 = true;
        subject = stripFieldEnd(subject, " REPORTED AT");
        body = subject + " @ " + body;
      }
    }
    if (!super.parseMsg(body, data)) return false;

    if (v3 && data.msgType == MsgType.RUN_REPORT) {
      setFieldList("CODE " + super.getProgram());
      data.strCode = subject;
    }
    return true;
  }

  /**
   * Internal method to check body or subject against and
   * paser required prefix strings
   * @param body string to be checked for prefix
   * @return null if prefix check fails.  Otherwise return
   * original body with the matching prefix removed
   */
  private String checkPrefix(String body) {
    if (prefix != null) {
      if (!body.startsWith(prefix)) return null;
      return body.substring(prefix.length()).trim();
    } else if (prefixPattern != null) {
      Matcher match = prefixPattern.matcher(body);
      if (!match.lookingAt()) return null;
      return body.substring(match.end()).trim();
    }
    return body;
  }

  @Override
  protected boolean isPageMsg(String body) {
    if (prefix != null || prefixPattern != null) return true;
    return super.isPageMsg(body);
  }
}
