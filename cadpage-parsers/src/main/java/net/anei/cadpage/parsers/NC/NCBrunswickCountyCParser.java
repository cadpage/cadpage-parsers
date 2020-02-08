package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCBrunswickCountyCParser extends FieldProgramParser {
  
  public NCBrunswickCountyCParser() {
    super("BRUNSWICK COUNTY", "NC", 
          "CALL:CODE_CALL! PLACE:PLACE! ADDR:ADDRCITY! XY:GPS! ID:ID! DATETIME:DATETIME! UNITS:UNIT! X:X! INFO:INFO! Notes:INFO2/N! TAC_Channel:CH! ProQA:INFO3/N! Map_Information:INFO3/N! Nearest_Intersection:INFO3/N! END");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("INFO3")) return new MyInfo3Field();
    return super.getField(name);
  }
  
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        String zip = match.group(2);
        city = p.getLastOptional(',');
        if (city.length() == 0 && zip != null) city = zip;
      }
      data.strCity = city;
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY ST";
    }
  }
  
  private static final Pattern INFO_HEADER_PTN = Pattern.compile(" *\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  public class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_HEADER_PTN.split(field)) {
        super.parse(line, data);
      }
    }
  }
  
  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field,  data);;
    }
  }
  
  private class MyInfo3Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0 || field.equals("None")) return;
      data.strSupp = append(data.strSupp, "\n", getRelativeField(0));
    }
  }

}
