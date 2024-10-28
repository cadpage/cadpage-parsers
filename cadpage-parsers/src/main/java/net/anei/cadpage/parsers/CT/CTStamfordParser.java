package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

/**
 * Stamford, CT
 */
public class CTStamfordParser extends FieldProgramParser {

  public CTStamfordParser() {
    super("STAMFORD", "CT",
          "ADDR CITY ST APT/D EMPTY CALL X NAME NAME/CS+? PHONE! INFO+");
  }

  @Override
  public String getFilter() {
    return "SFD.NoReply@StamfordCT.Gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Stamford 911-")) return false;
    return parseFields(body.split(",", -1), 9, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST")) return new MyStateField();
    if (name.equals("PHONE")) return new PhoneField("\\d{3}[- ]\\d{3,4}[- ]\\d{4}|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('.');
      if (pt >= 0) {
        data.strApt = append(data.strApt, "-", field.substring(pt+1).trim());
        field = field.substring(0,pt).trim();
      }
      data.strState = field;
    }

    @Override
    public String getFieldNames() {
      return "ST APT";
    }
  }

  private static final Pattern INFO_REJECT_PTN = Pattern.compile(".*(?:Multi-Agency LAW Incident #:|Automatic Case Number\\(s\\) issued).*");
  private static final Pattern INFO_PREFIX_PTN = Pattern.compile("(\\[\\d+\\]) *");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[(?:ProQA: Case Entry Complete|[ProQA: Case Entry Complete])\\]|[ProQA: Key Questions] >");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_REJECT_PTN.matcher(field).matches()) return;
      String prefix = null;
      Matcher match = INFO_PREFIX_PTN.matcher(field);
      if (match.lookingAt()) {
        prefix = match.group(1);
        field = field.substring(match.end());
      }
      match = INFO_JUNK_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end()).trim();
      if (field.length() == 0) return;
      String sep = ", ";
      if (prefix != null) {
        sep = "\n";
        field = prefix + ' ' + field;
      }
      data.strSupp = append(data.strSupp, sep, field);
    }
  }
}
