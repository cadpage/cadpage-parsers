package net.anei.cadpage.parsers.NV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NVClarkCountyEParser extends FieldProgramParser {

  public NVClarkCountyEParser() {
    super("CLARK COUNTY", "NV",
          "ID CODE_CALL SRC MAP ADDR UNIT INFO! INFO+ END");
  }

  @Override
  public String getFilter() {
    return "countydsp@clarkcountynv.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD")) return false;

    String[] flds = body.split("\\|", -1);
    if (flds.length > 5) {
      return parseFields(flds, data);
    }

    else {
      setFieldList("ADDR APT CITY X CODE CALL PRI MAP ID UNIT INFO");
      FParser fp = new FParser(body);
      parseAddress(fp.get(30), data);

      if (!fp.check("City: ")) return false;
      data.strCity = fp.get(35);

      if (!fp.check("X Street: ")) return false;
      data.strCross = fp.get(60);

      parseCall(fp.get(30), data);;

      if (!fp.check("Pri: ")) return false;
      data.strPriority = fp.get(30);

      if (!fp.check("Dist/Phan: ")) return false;
      data.strMap = fp.get(10);

      if (!fp.check("Inc #")) return false;
      data.strCallId = fp.get(20);

      if (!fp.check("Units: ")) return false;
      data.strUnit = fp.get(30);

      if (!fp.check("Comments:")) return false;
      parseExtra(fp.get(), data);
      return true;
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)- *(.*)");
  private void parseCall(String field, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(field);
    if (match.matches()) {
      data.strCode = match.group(1);
      field = match.group(2);
    }
    data.strCall = field;
  }

  private void parseExtra(String field, Data data) {
    field = stripFieldStart(field, "Comments");

    // Throw away anything between square brackets, and process
    // anything outside of them
    int pCnt = 0;
    int last = 0;
    for (int pt = 0; pt<field.length(); pt++) {
      int chr = field.charAt(pt);
      if (chr == '[') {
        if (pCnt++ == 0) parseExtraField(field.substring(last, pt), data);
      } else if (chr == ']') {
        if (--pCnt == 0) last = pt+1;
      }
    }

    if (pCnt == 0) {
      parseExtraField(field.substring(last), data);
    }
  }

  private static final Pattern EXTRA_FLD_PTN = Pattern.compile("FZ[A-Z0-9]+, *(.*)");
  private void parseExtraField(String field, Data data) {
    field = field.trim();
    field = stripFieldEnd(field, ",");
    if (field.length() == 0) return;
    if (field.startsWith("Class of Service")) return;
    Matcher match = EXTRA_FLD_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    data.strSupp = append(data.strSupp, "\n", field);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}-\\d{7}", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      parseCall(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      parseExtra(field, data);
    }
  }
}