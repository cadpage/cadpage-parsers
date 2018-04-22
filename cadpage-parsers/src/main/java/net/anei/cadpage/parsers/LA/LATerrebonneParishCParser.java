package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LATerrebonneParishCParser extends FieldProgramParser {
  
  public LATerrebonneParishCParser() {
    super("TERREBONNE PARISH", "LA", 
          "ID ADDR PLACE CODE CALL GPS1 GPS2 SKIP X INFO SRC UNIT CH! END");
  }
  
  @Override
  public String getFilter() {
    return "tpe911@tpe911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CFS - Unit Assigned - #"))  return false;
    if (body.endsWith("|")) body += " ";
    return parseFields(body.split(" \\| ", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{8}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:^| *; *)\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d: - ");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("\n").trim();
    }
  }
}
