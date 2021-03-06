# TPEdit

<!---
[![start with why](https://img.shields.io/badge/start%20with-why%3F-brightgreen.svg?style=flat)](http://www.ted.com/talks/simon_sinek_how_great_leaders_inspire_action)
--->
[![GitHub release](https://img.shields.io/github/release/elbosso/tpedit/all.svg?maxAge=1)](https://GitHub.com/elbosso/tpedit/releases/)
[![GitHub tag](https://img.shields.io/github/tag/elbosso/tpedit.svg)](https://GitHub.com/elbosso/tpedit/tags/)
[![GitHub license](https://img.shields.io/github/license/elbosso/tpedit.svg)](https://github.com/elbosso/tpedit/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/elbosso/tpedit.svg)](https://GitHub.com/elbosso/tpedit/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/elbosso/tpedit.svg)](https://GitHub.com/elbosso/tpedit/issues?q=is%3Aissue+is%3Aclosed)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/elbosso/tpedit/issues)
[![GitHub contributors](https://img.shields.io/github/contributors/elbosso/tpedit.svg)](https://GitHub.com/elbosso/tpedit/graphs/contributors/)
[![Github All Releases](https://img.shields.io/github/downloads/elbosso/tpedit/total.svg)](https://github.com/elbosso/tpedit)
[![Website elbosso.github.io](https://img.shields.io/website-up-down-green-red/https/elbosso.github.io.svg)](https://elbosso.github.io/)

This project is a tool to manage test plans ant to produce test protocols.
Test plans are persisted as simple XML files. 
The creation of the protocols is done using XSLT transformations. That way, it is possible
to create virtually any output format, though the focus of this project is to create PDF
documents.

This app is able to fully automatically create PDF documents with only those test cases
that are new since the last saving (and the ones that were modified since then)
or to create PDF focuments containing all test cases.

The app is also able to create interactive PDF documents that can be filled out on screen 
and printed afterwards. But it is also still possible  to create plain PDFs
that have to be printed out and filled out manually.

For more information visit my webpage starting [here](file:///home/elbosso/work/elbosso.github.io/tpedit_erstellt_interaktive_formulare.html#content). 

## Build

```
mvn compile exec:java
```

or 

```
mvn -U package assembly:single
```


