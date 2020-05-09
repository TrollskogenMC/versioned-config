# versioned-config
A Bukkit helper for creating versioned configuration files. It can be seen as a smart wrapper around the [FileConfiguration](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/configuration/file/FileConfiguration.html) object.

Usage
--

- [Install](#install)
- [Getting started](#getting-started)
  - [Creating our first command](#creating-our-first-command)
  - [Arguments](#arguments)
    - [Custom arguments](#custom-arguments)
    - [Invalid arguments](#invalid-arguments)
    - [Missing arguments](#missing-arguments)
    - [Validating based on previous arguments](#validating-based-on-previous-arguments)
    - [Tab completion](#tab-completion)
      - [Tab completion based on previous arguments](#tab-completion-based-on-previous-arguments)
    - [Default arguments](#default-arguments)
      - [Static arguments](#static-arguments)
      - [Dynamic arguments](#dynamic-arguments)
  - [Permissions](#permissions)
  
## Install
This package is available on Github Packages. Read more [here](https://help.github.com/en/packages/publishing-and-managing-packages/installing-a-package).
```xml
<dependency>
  <groupId>com.github.hornta</groupId>
  <artifactId>versioned-config</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
<br />

## Getting started
The configuration object is created by the ConfigurationBuilder so let's set it up.
