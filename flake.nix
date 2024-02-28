{
  inputs.astapkgs.url = "github:Astavie/astapkgs";
  inputs.astapkgs.inputs.nixpkgs.follows = "nixpkgs";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = { astapkgs, ... }: astapkgs.lib.package {

    # package = pkgs: with pkgs; ...

    devShell = pkgs: with pkgs; mkShell rec {

      packages = [
        jetbrains.idea-community
      ];

      buildInputs = [
        jetbrains.jdk
        libGL
        libpulseaudio
        flite
      ];

      shellHook = ''
        export LD_LIBRARY_PATH="${lib.makeLibraryPath buildInputs}"
      '';
      
    };
    
  } [ "x86_64-linux" ];
}
