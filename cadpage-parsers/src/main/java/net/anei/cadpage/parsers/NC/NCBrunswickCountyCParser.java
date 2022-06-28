package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NCBrunswickCountyCParser extends FieldProgramParser {
  
  public NCBrunswickCountyCParser() {
    super("BRUNSWICK COUNTY", "NC", 
          "CALL:CODE_CALL! PLACE:PLACE! ADDR:ADDRCITY! XY:GPS! ID:ID! DATETIME:DATETIME! UNITS:UNIT! UNIT/C+ X:X! INFO:INFO! Additional_Location_Details:INFO3/N! Notes:INFO2/N INFO/N+ TAC_Channel:CH! Map_Info:INFO3/N Nearest_Intersection:SKIP! END");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,no-reply@brunsco.net";
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(\\d\\d-\\d+) - Run Report;\n");
  private static final Pattern DELIM = Pattern.compile(";|\\s+(?=Additional Location Details:|Notes:|Code:|TAC Channel:|Map Info:|Nearest Intersection:|\\d+\\. )");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.lookingAt()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      body = body.substring(match.end()).trim();
      for (String line : body.split("\n")) {
        if (line.contains("Dispatched")) line = line.replace("; ", "\n");
        data.strSupp = append(data.strSupp, "\n", line);
      }
      return true;
    }
    return parseFields(DELIM.split(body), data);
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
      if (field.equals("None")) return;
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
