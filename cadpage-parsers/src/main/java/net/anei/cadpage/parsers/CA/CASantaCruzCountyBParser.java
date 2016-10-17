package net.anei.cadpage.parsers.CA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CASantaCruzCountyBParser extends FieldProgramParser {

  public CASantaCruzCountyBParser() {
    this("SANTA CRUZ COUNTY", "CA");
  }

  public CASantaCruzCountyBParser(String defCity, String defState) {
    super(defCity, defState, 
          "CALL! ADDRCITY! APT_PLACE X! Map:MAP! ID UNIT! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "czu.ecc@fire.ca.gov";
  }

  @Override
  public String getAliasCode() {
    return "CASantaCruzCountyB";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressField();
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals(("ID"))) return new IdField("Inc# *(\\d*)", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_BAD_NUMBER = Pattern.compile(" *\\b99\\d{4}\\b *");

  private class MyAddressField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("_", " ");
      if (field.startsWith("=L(")) {
        super.parse(field, data);
        return;
      }
      field = ADDR_BAD_NUMBER.matcher(field).replaceAll(" ").trim();
      int pi = field.indexOf("@");
      if (pi >= 0) {
        String p1 = field.substring(0, pi);
        String p2 = field.substring(pi + 1, field.length());
        p1 = p1.trim();
        p2 = p2.trim();
        data.strPlace = p1;
        super.parse(p2, data);
      } else if ((pi = field.indexOf('(')) >= 0)  {
        data.strPlace = stripFieldEnd(field.substring(pi+1).trim(), ")");
        super.parse(field.substring(0,pi).trim(), data);
      } else {
        if (field.equals("NO TEXT")) return;
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
  
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.contains(" ")) {
        data.strPlace = append(data.strPlace, " - ", field);
      } else {
        data.strApt = append(data.strApt, "-", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }
  

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field.replace(";", "/");
      int pi = field.indexOf("<a href=");
      if (pi >= 0) {
        setGPSLoc(field.substring(pi), data);
        field = field.substring(0, pi).trim();
      } 
      if (field.equals("NO TEXT")) return;
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
}
