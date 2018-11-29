package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORYamhillCountyBParser extends FieldProgramParser {

  public ORYamhillCountyBParser() {
    super(CITY_CODES, "YAMHILL COUNTY", "OR",
          "ID ADDR APT EMPTY CITY ADDR X MAP EMPTY CODE CALL EMPTY EMPTY UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "YCOM@ycom.org";
  }
  private static final Pattern DELIM = Pattern.compile("\\*[ \n]");

  @Override
  public boolean parseMsg(String body, Data data) {

    if (!body.startsWith("YCOM:")) return false;
    body = body.substring(5).trim();
    if (body.endsWith("*")) body += ' ';
    if (super.parseFields(DELIM.split(body), data)) {
      if (data.strCode.equals(data.strCall)) data.strCode = "";
      return true;
    }
    data.parseGeneralAlert(this, body.trim());
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("COVE ORCHARD")) city = "YAMHILL";
    if (city.equalsIgnoreCase("MCCOY")) city = "RICKREALL";
    return super.adjustMapCity(city);
  }
  
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "YCTY",   "YAMHILL COUNTY",
        "CARL",   "CARLTON",
        "COVE",   "COVE ORCHARD",
        "YAM",    "YAMHILL",
      "PCTY",   "POLK COUNTY",
        "MCCY",   "MCCOY",
      "WCTY",   "WASHINGTON COUNTY"
  });
  
}
