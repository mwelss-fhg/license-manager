import { Component, OnInit } from '@angular/core';
import { LicenseRtuMetaService } from '../license-rtu-meta.service';

@Component({
  selector: 'app-license-rtu-editor',
  templateUrl: './license-rtu-editor.component.html',
  styleUrls: ['./license-rtu-editor.component.scss']
})

/**
 * RTU form editor
 * Will construct a minimal ODRL RTU file for
 * use by LUM.
 */
export class LicenseRtuEditorComponent implements OnInit {

  title = 'Acumos Right to Use Editor';
  jsonSchema: any;
  formLayout: any = {};
  jsonData: any = {};
  downloadType = 'txt';
  jsonFormOptions: any;


  constructor(private service: LicenseRtuMetaService) { }

  ngOnInit() {
    // load assets/rtu-schema.json
    this.service.getSchema().subscribe((data) => {
      this.jsonSchema = data;
    });

    this.formLayout = [
      { type: 'flex', 'flex-flow': 'row wrap' },
      { key: 'uid' },
      {
        key: 'permission', type: 'fieldset',
        items: {
          type: 'div',
          displayFlex: true,
          'flex-direction': 'row',
          items: [
            'permission[].target',
            {
              key: 'permission[].assigner',
              type: 'fieldset',
              items: [
                'permission[].assigner.uid',
                'permission[].assigner.vcard:fn',
                'permission[].assigner.vcard:hasEmail',

              ]
            },
            {
              key: 'permission[].assignee',
              type: 'fieldset',
              items: [
                'permission[].assignee.uid',
                'permission[].assignee.vcard:fn',
                'permission[].assignee.vcard:hasEmail',
              ]
            },
            {
            key: 'permission[].action',
            type: 'array',
            items: [
              { key: 'permission[].action[].action'
              },
              { key: 'permission[].action[].refinement'
              }
            ]              // type: 'div',
              // displayFlex: true,
              // 'flex-direction': 'row',
              // items: [
              //   {key: 'permission[].action'},

              //   // 'permission[].action.refinement'

              // ]

            },
            'permission[].constraint'
          ]
        }
      }
    ];
  }

  showExample(id: string) {
    this.service.getSample(id).subscribe((data) => {
      this.jsonData = data;
    });
  }

}
