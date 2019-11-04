/*
 * Copyright 2018 Litsec AB
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
package se.litsec.bankid.demo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Model class for representing a selectable language in the UI.
 * 
 * @author Martin Lindström (martin@litsec.se)
 */
@Data
@NoArgsConstructor
@ToString
public class UiLanguage {

  private String languageTag;
  private String text;
}
