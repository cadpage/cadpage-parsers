package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORYamhillCountyBParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\\* ");

  public ORYamhillCountyBParser() {
    super(CITY_CODES, "YAMHILL COUNTY", "OR",
          "ID ADDR APT EMPTY CITY ADDR X MAP EMPTY EMPTY CALL EMPTY EMPTY UNIT! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "YCOM@ycom.org";
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("COVE ORCHARD")) city = "YAMHILL";
    if (city.equalsIgnoreCase("MCCOY")) city = "RICKREALL";
    return super.adjustMapCity(city);
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    if (!body.startsWith("YCOM:")) return false;
    body = body.substring(5).trim();
    if (body.endsWith("*")) body += ' ';
    if (super.parseFields(DELIM.split(body), data)) return true;
    data.parseGeneralAlert(this, body.trim());
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "YCTY",   "YAMHILL COUNTY",
        "COVE", "COVE ORCHARD",
      "PCTY",   "POLK COUNTY",
        "MCCY",  "MCCOY"
  });
  
}
