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

using Google.Apis.Dfareporting.v2_6;

namespace DfaReporting.Samples {
  /// <summary>
  /// This abstract class represents a code example.
  /// </summary>
  abstract class SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public abstract string Description {
      get;
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">A Dfa Reporting service instance.</param>
    public abstract void Run(DfareportingService service);

    protected string _T(string prompt) {
#if INTERACTIVE
      Console.Write(prompt + " : ");
      return Console.ReadLine();
#else
      return prompt;
#endif
    }
  }
}
