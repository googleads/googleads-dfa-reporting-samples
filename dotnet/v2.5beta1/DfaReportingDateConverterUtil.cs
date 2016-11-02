/*
 * Copyright 2015 Google Inc
 *
 * Licensed under the Apache License, Version 2.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

using System;

namespace DfaReporting.Samples {
  /// <summary>
  /// This simple utility converts <c>DateTime</c> objects to the <c>string</c>
  /// formats that the DCM/DFA Reporting and Trafficking API expects dates to
  /// be in.
  /// </summary>
  internal static class DfaReportingDateConverterUtil {
    private const string DateFormat = "yyyy-MM-dd";
    private const string DateTimeFormat = "yyyy-MM-dd HH:mm:ss.FFFZ";

    /// <summary>
    /// Takes a <c>DateTime</c> object and converts it to the proper date-only
    /// <c>string</c> format for the DCM/DFA Reporting and Trafficking API.
    /// </summary>
    /// <param name="date">The date to be converted.</param>
    /// <returns>The given date in the proper format.</returns>
    public static string convertToDateString(DateTime date) {
      return date.ToString(DateFormat);
    }

    /// <summary>
    /// Takes a <c>DateTime</c> object and converts it to the proper datetime
    /// <c>string</c> format for the DCM/DFA Reporting and Trafficking API.
    /// </summary>
    /// <param name="date">The date to be converted.</param>
    /// <returns>The given date in the proper format.</returns>
    public static string convertToDateTimeString(DateTime date) {
      return date.ToString(DateTimeFormat);
    }
  }
}