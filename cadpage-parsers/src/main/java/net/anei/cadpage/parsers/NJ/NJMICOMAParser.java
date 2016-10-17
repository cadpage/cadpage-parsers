package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * This may be obsolete and replaced by NJMICOMBParser, but we are not yet
 * convinced of that
 */

public class NJMICOMAParser extends MsgParser {
  
  private static final Pattern CALL_DELIM = Pattern.compile("(?:-[AB]LS *)?\\d\\d:\\d\\d");
  
  public NJMICOMAParser() {
    super("", "NJ");
    setFieldList("UNIT ID CITY ADDR PLACE APT X CALL");
  }
  
  @Override
  public String getFilter() {
    return "miccom@nnjmicu.org,miccom@nnjems.org,cadsmtp@nnjems.org";
  }
  
  @Override
  public String getLocName() {
    return "MICCOM (northern NJ), NJ";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD")) return false;
    
    Parser p = new Parser(body);
    String type = p.get(' ');
    if (!type.equals("CANCEL:") && !type.equals("RESPOND:")  && !type.equals("@")) {
      data.strUnit = type;
      type = p.get(' ');
    }
    if (! type.equals("RESPOND:")) {
      if (!type.equals("CANCEL:") && !type.equals("@")) return false;
      data.strCall = "RUN REPORT";
      data.strPlace = type + " " + p.get();
      return true;
    }
    p.get('#');
    data.strCallId = p.get(' ');
    data.strCity = p.get('*');
    
    parseAddress(p.get('*'), data);
    if (data.strAddress.length() == 0) return false;
    
    data.strPlace = p.get('*');
    data.strApt = p.get('*');
    data.strCross = p.get('*');
    data.strCall = p.get(CALL_DELIM);
    
    for (String suffix : new String[]{" BOR", " BORO"}) {
      if (data.strCity.endsWith(suffix)) {
        data.strCity = data.strCity.substring(0, data.strCity.length()-suffix.length());
      }
    }
    
    return true;
  }
}
