package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
public class DispatchA69Parser extends FieldProgramParser {

  protected DispatchA69Parser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/RR ID1 CALL ADDR INFO/R! INFO/N+ " +
          "| ID2! INCIDENT_NAME:PLACE! CODE_CALL! ADDRCITY! INFO/N+? GPS! RESOURCES:UNIT! COMMAND:CH! TAC:CH/L! AIR:CH/L! GRD:CH/L! END " +
          "| ID3 MAP? CODE_CALL ADDR PLACE GPS! Resources:UNIT% INFO/N+ Remarks:INFO/N+ )");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern DELIM = Pattern.compile("\n\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Page")) return false;
    if (body.startsWith("CLOSE:")) {
      setSelectValue("RR");
      body = body.substring(6).trim();
      return parseFields(body.split(";"), data);
    }
    else {
      setSelectValue("");
      return parseFields(DELIM.split(body), data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("Inc# (\\d{6})", true);
    if (name.equals("ID2")) return new IdField("INCIDENT #(\\d{6})", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ID3")) return new IdField("Incident #(\\d{6}|)", true);
    if (name.equals("MAP")) return new MapField("RespArea *(\\S+)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      if (field.endsWith(")")) {
        pt = field.indexOf('(');
        if (pt >= 0) {
          String place = field.substring(pt+1, field.length()-1).trim();
          data.strPlace = append(data.strPlace, " - ", place);
          field = field.substring(0,pt).trim();
        }
      }
      pt = field.lastIndexOf(',');
      if (pt < 0)  abort();
      data.strCity = field.substring(pt+1).trim().replace('_', ' ');
      field = field.substring(0,pt).trim();
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("BTWN " )) {
        data.strCross = field.substring(5).trim();
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }

  private static final Pattern INFO_CODE_PTN = Pattern.compile("Dispatch +([A-Z0-9]+)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NO TEXT")) return;
      Matcher  match = INFO_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
      } else {
        super.parse(field, data);;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }

  private static final Pattern GPS_PTN1 = Pattern.compile("(?:.*\n)?http://maps.google.com/\\?q=([-+]?\\d+\\.\\d{4,},[-+]?\\d+\\.\\d{4,})");
  private static final Pattern GPS_PTN2 = Pattern.compile("Lat: (.*); Long: (.*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN1.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        return;
      }
      int pt = field.indexOf('\n');
      if (pt >= 0) field = field.substring(0, pt).trim();
      match = GPS_PTN2.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        return;
      }
      if (!field.startsWith("Lat:")) abort();
    }
  }
}
