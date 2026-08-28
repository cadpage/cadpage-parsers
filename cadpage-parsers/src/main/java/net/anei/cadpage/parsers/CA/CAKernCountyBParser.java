package net.anei.cadpage.parsers.CA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

  public class CAKernCountyBParser extends FieldProgramParser {

    public CAKernCountyBParser() {
      super("KERN COUNTY", "CA",
            "CALL:CALL! PLACE_NAME:PLACE! GPS:GPS! ADDR:ADDR! ( CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! " +
                                                             "| City:CITY! PRI:PRI! UPDATE_TIME:DATETIME " +
                                                             ") INFO:INFO! INFO/N+");
    }

    @Override
    public String getFilter() {
      return "@us.af.mil";
    }

    @Override
    public boolean parseMsg(String subject, String body, Data data) {
      if (!subject.startsWith("DISPATCH - ") && !subject.startsWith("RE-PAGE UPDATE:")) return false;
      return parseFields(body.split("\n"), data);
    }

    private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");

    @Override
    public Field getField(String name) {
      if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
      if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
      if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
      return super.getField(name);
    }
  }
