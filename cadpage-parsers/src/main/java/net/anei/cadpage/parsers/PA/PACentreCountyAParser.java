package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACentreCountyAParser extends FieldProgramParser {

  public PACentreCountyAParser() {
    super("CENTRE COUNTY", "PA", "BOX CALL ADDR! NAME INFO+");
  }
  
  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com,CENTRECOUNTY911";
  }

  private static final Pattern NON_ASCII = Pattern.compile("[^\\x00-\\x7f]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // check subj
    if (subject.length() > 0 && !subject.equals("Centre County Alerts")) return false;

    int pt = body.indexOf('.');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // kill non ascii
    body = NON_ASCII.matcher(body).replaceAll("");

    return parseFields(body.split(";", -1), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("BOX ?(\\d*)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("NAME")) return new NameField("NAME-(.*)|", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional("[_");
      p.getLastOptional(" _"); // remove extra city sometimes present in intersection notation
      data.strPlace = p.getLastOptional("[@");
      data.strApt = p.getLastOptional("[#");
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }

  }
}
