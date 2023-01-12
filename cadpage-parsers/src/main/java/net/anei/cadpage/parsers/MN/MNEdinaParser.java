package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNEdinaParser extends GroupBestParser {

  public MNEdinaParser() {
    super(new MNEdinaAParser(), new MNEdinaBParser());
  }
}