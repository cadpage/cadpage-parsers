package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBexarCountyDParser extends FieldProgramParser {

   public TXBexarCountyDParser() {
     super("BEXAR COUNTY", "TX",
           "CALL:CALL! ADDR:ADDR! CITY:CITY! PRI:PRI! DATE:DATE! TIME:TIME! LAT:GPS1! LNG:GPS2! INFO:INFO/N+");
   }

   @Override
   public String getFilter() {
     return "mci@alerts.strac.org";
   }

   @Override
   public int getMapFlags() {
     return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
   }

   @Override
   protected boolean parseMsg(String body, Data data) {
     return parseFields(body.split("\n"), data);
   }

   @Override
   public Field getField(String name) {
     if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
     if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
     return super.getField(name);
   }
}
