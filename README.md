Welcome to the CropScreenRotateMove wiki!

## Add it in your root settings.gradle at the end of repositories:

<code> ```dependencyResolutionManagement 
{ 
repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) 
repositories {
 mavenCentral() 
 maven { url 'https://jitpack.io' } 
} 
} ```</code>

### Add the dependency

`dependencies {`
	        `implementation 'com.github.dips25:CropScreenRotateMove:v1.0-beta'`
	     `}`

### Use in Manifest(keep width/height wrap_content always)

`* <activity`
`*             android:name="com.layout.swiiiipe.myapplication.CropActivity"`
`*             android:exported="true">`

`*         </activity>`


### Start the Activity

`Intent intent = new Intent(this , CropActivity.class);`
                            `intent.putExtra("data",uri.toString());`
                            `startActivity(intent);`![Screenshot_20250502_111217](https://github.com/user-attachments/assets/b4217f6a-4313-4052-85f0-5702eae7271d)
![Screenshot_20250502_111143](https://github.com/user-attachments/assets/b4c4c821-4291-49b6-b60d-39dc866b47dd)
![Screenshot_20250502_111127](https://github.com/user-attachments/assets/0c332d36-5b16-4e1d-9ff2-e69a3b86cc5a)
![Screenshot_20250502_111033](https://github.com/user-attachments/assets/3ee372b1-a637-40bf-8a7a-2e9fbc48bd09)
