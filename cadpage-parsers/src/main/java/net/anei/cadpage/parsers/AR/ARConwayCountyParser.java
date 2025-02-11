package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ARConwayCountyParser extends DispatchSouthernParser {

  public ARConwayCountyParser() {
    super(CITY_LIST, "CONWAY COUNTY", "AR", DSFLG_ADDR | DSFLG_OPT_BAD_PLACE | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  private static final String[] CITY_LIST = new String[] {
      "CONWAY COUNTY",

      // Cities
        "MORRILTON",
        "OPPELO",
        "PLUMERVILLE",

        // Town
        "MENIFEE",

        // Census-designated places
        "CENTER RIDGE",
        "HATTIEVILLE",
        "JERUSALEM",
        "SPRINGFIELD",

        // Other unincorporated communities
        "BLACKWELL",
        "CLEVELAND",
        "FORMOSA",
        "JERUSALEM",
        "LANTY",
        "MOUNT OLIVE",
        "PLEASANT HILL",
        "PONTOON",
        "SOLGOHACHIA",
        "WINROCK"
  };
}
