package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class COLarimerCountyEParser extends FieldProgramParser {
  
  public COLarimerCountyEParser() {
    super("LARIMER COUNTY", "CO", "DISPATCH:CALL! ADDR:ADDR! MAP! INFO UNITS:UNIT! TAC:CH! GPS! Incident:ID Case:ID/L END");
  }
  
  @Override
  public String getFilter() {
    return "noreply@larimercrisp.org";
  }
  
  private static final Pattern DELIM = Pattern.compile(" (?=ADDR:)| \\| ");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name); 
  }

  private static final Pattern ADDR_PTN = Pattern.compile("(.*?)#(.*) btwn\\b *(.*)");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");

  private class MyAddressField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      parseAddress(match.group(1).trim(), data);
      String apt = MBLANK_PTN.matcher(match.group(2).trim()).replaceAll(" ");
      data.strApt = append(data.strApt, "-", apt);
      data.strCross = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT X";
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "MAP:");
      if (field.equals("NOT FOUND")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("X: *(\\d+)(\\d{6}) +Y: *(\\d+)(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return;
      field = match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4);
      super.parse(field, data);
    }
  }
}
