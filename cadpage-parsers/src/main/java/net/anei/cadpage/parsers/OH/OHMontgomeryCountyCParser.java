package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMontgomeryCountyCParser extends FieldProgramParser {
  
  public OHMontgomeryCountyCParser() {
    super("MONTGOMERY COUNTY", "OH", 
          "CODE CALL ADDR X! PLACE TEXT:INFO? Unit:UNIT! UNIT/C+");
  }
  
  @Override
  public String getFilter() {
    return "69250";
  }
  
  private static final Pattern LINE_BRK_PTN = Pattern.compile(" *\n *");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    if (body.startsWith("<https://")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
      pt = body.indexOf("\nYOUR ACCOUNT");
      if (pt >= 0) body = body.substring(0,pt).trim();
    }

    if (!body.startsWith("MCSO Dispatch Alerts:DISPATCH:")) return false;
    body = body.substring(30).trim();
    
    body = LINE_BRK_PTN.matcher(body).replaceAll(" ");
    int pt = body.indexOf(" - From");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace(" Units:", " Unit:");
    return super.parseFields(body.split(","), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("[-A-Z0-9]+", true);
    if (name.equals("X")) return new CrossField("\\((.*?)\\)?|", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new  MyUnitField();
    return super.getField(name);
  }
  
  private static final Pattern PLACE_NAME_PHONE_PTN = Pattern.compile("(.*?)[/ ]+((?:\\d{3}[- ])?\\d{3}[- ]\\d{4})");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NO FURTHER INFORMATION")) return;
      Matcher match = PLACE_NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strName = match.group(1);
        data.strPhone = match.group(2);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE NAME PHONE";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "(MTF)");
      for (String part : field.split("\\\\")) {
        part = part.trim();
        if (part.equals("PROQA RECOMMENDS DISPATCH AT THIS TIME")) {
        } else if (part.startsWith("NAME:")) {
          part = part.substring(5).trim();
          if (part.startsWith("LIFE LINE") ||  part.startsWith("MEDICAL ALERT")) {
            data.strSupp = append(data.strSupp, "/n", part);
          } else if (data.strName.length() > 0) {
            data.strPlace = cleanWirelessCarrier(part);
          } else {
            data.strName = cleanWirelessCarrier(part);
          }
        } else if (part.startsWith("PH:")) {
          data.strPhone = append(data.strPhone, " / ", part.substring(3).trim());
        } else if (part.startsWith("ADR:")) {
        } else if (part.startsWith("SOURCE:")) {
        } else {
          data.strSupp = append(data.strSupp, "/n", part);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME  PHONE PLACE INFO";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" ", "");
      super.parse(field, data);
    }
  }
}
