Pod::Spec.new do |spec|
    spec.name                     = 'sdknrstations'
    spec.version                  = '3.0.4-SNAPSHOT'
    spec.homepage                 = 'https://github.com/intsoftdev/NRStationsKMP'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'KMP Stations'
    spec.vendored_frameworks      = 'build/cocoapods/framework/sdknrstations.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '12.4'
                
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':sdknrstations',
        'PRODUCT_MODULE_NAME' => 'sdknrstations',
    }
                
    spec.script_phases = [
        {
            :name => 'Build sdknrstations',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end