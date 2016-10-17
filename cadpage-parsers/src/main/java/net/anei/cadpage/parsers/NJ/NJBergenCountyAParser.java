package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJBergenCountyAParser extends FieldProgramParser {

  public NJBergenCountyAParser() {
    super("BERGEN COUNTY", "NJ", "DISPATCH_ID:ID! CALL_TYPE:CALL! ADDRESS:ADDR! RESPONSE_ORDER:UNIT! DESCRIPTION:INFO+");
  }
  
  @Override
  public String getFilter() {
    return "wcl_njfd@infomap911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("InfoMap911 Dispatch:")) return false;

    return super.parseFields(body.split("\\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      // field contains ADDR, CITY, ST so split it like that
      String[] fields = field.split(", ");
      if (fields.length != 3) abort();
      data.strAddress = fields[0].trim();
      data.strCity = fields[1].trim();
      // if state != NJ then use it
      String st = fields[2].trim();
      if (!st.equals("NJ")) data.strState = st;
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST";
    }
  }

  //CODE (CALL)
  private static Pattern CODE_CALL = Pattern.compile("(.+) \\((.*)\\)");

  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = CODE_CALL.matcher(field);
      if (!mat.matches()) abort();
      data.strCode = mat.group(1);
      data.strCall = mat.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static Pattern DASHES = Pattern.compile("-*|IF YOU ARE RESPONDING TO THIS CALL,.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = DASHES.matcher(field);
      if (!mat.matches()) {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
  }
}
