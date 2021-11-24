package net.anei.cadpage.parsers.dispatch;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class DispatchA85Parser extends FieldProgramParser {

  public DispatchA85Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "ADDR/S6 CITY DATE TIME UNIT UNIT/C UNIT/C APT CALL/SDS! ID? INFO/N+");
  }

  private Set<String> unitSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Subject is  no longer required
    data.strCall = subject;

    // Clean out GNUGPL headers and trailers
    if (body.startsWith("X-Mailer:")) {
      int pt = body.indexOf("\nContent-Disposition:");
      if (pt < 0) return false;
      pt = body.indexOf('\n', pt+14);
      if (pt < 0) return false;
      while (pt < body.length() && body.charAt(pt) == '\n') pt++;
      int ept = body.indexOf('\n', pt);
      if (ept < 0) ept = body.length();
      body = body.substring(pt, ept).trim();
    }
    unitSet.clear();
    return parseFields(body.split("~"), data);
  }

  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)", Pattern.CASE_INSENSITIVE);
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("\\d+");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strApt.isEmpty()) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strApt = field;
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (unitSet.add(field)) data.strUnit = append(data.strUnit, ",", field);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase(data.strCall)) return;
      super.parse(field, data);
    }
  }

  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile(" *(?:\\.{3,}\\d*|_+Add below this line.*)$");
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("^\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b[ .]*");
  private static final Pattern JUNK_PTN = Pattern.compile("_+Add below this line.*|\\S+ Added|Fire Pager call at.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line =  line.trim();
        line = TRAIL_JUNK_PTN.matcher(line).replaceFirst("");
        line = LEAD_JUNK_PTN.matcher(line).replaceFirst("");
        if (JUNK_PTN.matcher(line).matches()) continue;
        data.strSupp = append(data.strSupp, "\n", line);
      }

    }
  }
}
