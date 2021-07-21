package net.anei.cadpage.parsers.dispatch;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchX01Parser extends FieldProgramParser {

  public DispatchX01Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
        "AgencyCode:SRC! " +
        "CallPriority:PRI " +
        "CallTakerComments:INFO " +
        "CallZoneCode:MAP " +
        "CityCode:CITY " +
        "IncidentNature:CALL! " +
        "LongTermCallID:ID! " +
        "RespondToAddress:ADDR! " +
        "ResponsibleUnitNumber:UNIT " +
        "StatusCodeOfCall:SKIP " +
        "TimeDateReported:TIMEDATE% " +
        "UnitNumber:UNIT " +
        "XCoordinateGeobase:GPS1/d " +
        "YCoordinateGeobase:GPS2/d",
        FLDPROG_XML);
  }

  private Set<String> unitSet = new HashSet<>();

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    unitSet.clear();
    try {
      return super.parseHtmlMsg(subject, body, data);
    } finally {
      unitSet.clear();
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strSource.contains(field)) {
        data.strSource = append(data.strSource, ",", field);
      }
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (unitSet.add(field)) {
        data.strUnit = append(data.strUnit, ",", field);
      }
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\b\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4} - (?:D\\d+|[A-Z]\\.) [A-Za-z]+\\b *| *\n *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
