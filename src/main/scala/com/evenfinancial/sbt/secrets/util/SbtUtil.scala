package com.evenfinancial.sbt.secrets.util

import sbt._

object SbtUtil {

  lazy val consoleLogger = ConsoleLogger()

  def promptForOverwrite(file: File)(overwrite: => Unit) {
    if (file.exists) {
      val action = SimpleReader.readLine(s"${file.getName} already exists: (o)verwrite, (b)ackup or (s)kip? [o] ")

      action.get match {
        case "" | "o" | "overwrite" => {
          consoleLogger.warn(s"overwriting ${file.getName}")
          overwrite
        }
        case "b" | "backup" => {
          val backup = fileWithSuffix(file, ".backup")
          consoleLogger.info(s"backing up ${file.getName} to ${backup.getName}")
          IO.transfer(file, backup)
          overwrite
        }
        case "s" | "skip" => {
          consoleLogger.warn(s"not overwriting ${file.getName}")
        }
        case invalid => {
          consoleLogger.error(s"Invalid response: ${invalid}")
          promptForOverwrite(file)(overwrite)
        }
      }
    } else {
      overwrite
    }
  }

  def fileWithSuffix(file: File, suffix: String): File = {
    file.getParentFile / (file.getName + suffix)
  }

}
