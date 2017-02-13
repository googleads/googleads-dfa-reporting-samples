<?php
/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

require_once dirname(__DIR__) . '/htmlHelper.php';

/**
 * Base class for all examples, contains helper methods to support gathering
 * input and rendering results.
 */
abstract class BaseExample {
  /**
   * Service definition for DFA Reporting
   * @var Google_Service_Dfareporting
   */
  protected $service;

  /**
   * The headers to be used in the results table
   * @var array
   */
  protected $headers;

  /**
   * Inject the dependency.
   * @param Google_Service_Dfareporting $service
   *     An authenticated instance of Google_Service_Dfareporting.
   */
  public function __construct(Google_Service_Dfareporting $service) {
    $this->service = $service;
    $this->headers = $this->getResultsTableHeaders();
  }

  /**
   * Contains the logic of the example.
   */
  abstract protected function run();

  /**
   * Executes the example, checks if the examples requires parameters and
   * request them before invoking run.
   */
  public function execute() {
    if (count($this->getInputParameters())) {
      if ($this->isSubmitComplete()) {
        $this->formValues = $this->getFormValues();
        $this->run();
      } else {
        $this->renderInputForm();
      }
    } else {
      $this->run();
    }
  }

  /**
   * Returns a display name for the example.
   * @return string
   */
  abstract public function getName();

  /**
   * Returns the list of input parameters of the example.
   * To be overridden by examples that require parameters.
   * @return array
   */
  protected function getInputParameters() {
    return [];
  }

  /**
   * Returns the list of headers to be shown in the results table.
   * To be overridden by examples that require table headers.
   * @return array
   */
  protected function getResultsTableHeaders() {
    return [];
  }

  /**
   * Renders an input form to capture the example parameters.
   */
  protected function renderInputForm() {
    $parameters = $this->getInputParameters();
    if (count($parameters)) {
      printf('<h2>Enter %s parameters</h2>', $this->getName());
      print '<form method="POST" enctype="multipart/form-data"><fieldset>';
      foreach ($parameters as $parameter) {
        $name = $parameter['name'];
        $display = $parameter['display'];

        if(isset($_POST[$name])) {
          $currentValue = $_POST[$name];
        } else {
          $currentValue = '';
        }

        // If this is a file parameter, generate a file input element
        if($parameter['file']) {
          $inputType = ' type="file"';
        }

        printf('%s: <input name="%s" value="%s"%s>', $display,
            $name, $currentValue, $inputType);

        if ($parameter['required']) {
          print '*';
        }

        print '<br>';
      }
      print '</fieldset>*required<br>';
      print '<input type="submit" name="submit" value="Submit"/>';
      print '</form>';
    }
  }

  /**
   * Checks if the form has been submitted and all required parameters are
   * set.
   * @return bool
   */
  protected function isSubmitComplete() {
    if (!isset($_POST['submit'])) {
      return false;
    }

    foreach ($this->getInputParameters() as $parameter) {
      if ($parameter['required']) {
        if($parameter['file']) {
          return !empty($_FILES[$parameter['name']]);
        } else {
          return !empty($_POST[$parameter['name']]);
        }
      }
    }

    return true;
  }

  /**
   * Retrieves the submitted form values.
   * @return array
   */
  protected function getFormValues() {
    $input = [];

    foreach ($this->getInputParameters() as $parameter) {
      if($parameter['file'] && isset($_FILES[$parameter['name']])) {
        $input[$parameter['name']] = $_FILES[$parameter['name']];
      } else if (isset($_POST[$parameter['name']])) {
        $input[$parameter['name']] = $_POST[$parameter['name']];
      }
    }

    return $input;
  }

  /**
   * Prints out the header for the results table.
   * @param string $title
   *     A friendly name for the results table
   */
  protected function printResultsTableHeader($title) {
    printf('<h3>%s</h3>', $title);
    print '<table class="resultsTable"><tr>';

    foreach ($this->headers as $name => $value) {
      printf('<th>%s</th>', $value);
    }

    print '</tr>';
  }

  /**
   * Prints out a row of data for the results table.
   * @param array $row
   *     Values representing a single row in the results table
   */
  protected function printResultsTableRow($row) {
    print '<tr>';

    foreach ($this->headers as $name => $value) {
      print '<td>';
      print_r($row[$name]);
      print '</td>';
    }

    print '</tr>';
  }

  /**
   * Prints out a "No results" row for the results table.
   */
  protected function printNoResultsTableRow() {
    print '<tr>';
    printf('<td class="noResults" colspan="%s">No results.</td>',
        count($this->headers));
    print '</tr>';
  }

  /**
   * Prints out the footer for the results table.
   */
  protected function printResultsTableFooter() {
    print '</table><br>';
  }

  /**
   * Convenience method to print an entire results table at once.
   * @param string $title
   *     A friendly name for the results table
   * @param array $rows
   *     Values representing multiple rows in the results table
   */
  protected function printResultsTable($title, $rows) {
    $this->printResultsTableHeader($title);

    if(empty($rows)) {
      $this->printNoResultsTableRow();
    } else {
      foreach ($rows as $row) {
        $this->printResultsTableRow($row);
      }
    }

    $this->printResultsTableFooter();
  }
}
